package com.hk.gs.receipt.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.receipt.mapper.ReceiptMapper;
import com.hk.gs.sell.mapper.SellMapper;
import com.hk.gs.sell.mapper.SellReturnMapper;
import com.hk.gs.util.CommUtil;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("receiptService")
public class ReceiptService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
	
	@Resource(name = "receiptMapper")
    private ReceiptMapper receiptMapper;
	
	@Resource(name = "sellMapper")
    private SellMapper sellMapper;
	
	@Resource(name = "sellReturnMapper")
    private SellReturnMapper sellReturnMapper;
	
	/**
	 * 영수증 발행전 조회
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> previewReceipt(Map<String, Object> map) throws Exception {
    	return receiptMapper.getReceiptList(map);		
    }
	
	/**
	 * 영수증 기본정보
	 * @since 2017.12.10
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getCustInfo(Map<String, Object> map) throws Exception {		
    	return receiptMapper.getCustInfo(map);		
    }
	
	/**
	 * 영수증 엑셀 시트 목록
	 * @since 2017.12.24
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtList(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtList(map);		
    }
	
	/**
	 * 영수증 엑셀 시트 목록
	 * @since 2017.12.24
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtListLv3(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtListLv3(map);		
    }
	
	/**
	 * 영수증 하단의 금액 합계
	 * @since 2017.12.24
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getSellTotalPrice(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellTotalPrice(map);		
    }
	
	/**
	 * 판매가액 등록 및 수정
	 * @since 2018.01.21
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setSellList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> tempMap;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> priceInfo = new HashMap<String, Object>();
		Map<String, Object> priceDate = new HashMap<String, Object>();
		int iProdCnt = 0;
		String sInputPrice = "";
		String sGetPrice = "";
		String sMaxPriceStartDt = "";
		String sTmpDt = "";
		String sOrgPrice = "";
		int iDfDt = 0;
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		Calendar cal = Calendar.getInstance();
		
		List<Map<String, Object>> sellist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			sellist.add(tempMap);
			 
			paramMap.put("prod_nm", tempMap.get("prod_nm"));
			paramMap.put("unit_nm", tempMap.get("unit_nm"));
			paramMap.put("prod_typ", tempMap.get("prod_typ"));
			paramMap.put("prod_price", tempMap.get("prod_price"));
			paramMap.put("cust_nm", tempMap.get("cust_nm"));
			paramMap.put("tax_yn", tempMap.get("tax_yn"));
    		
			paramMap.put("cust_no", tempMap.get("cust_no"));
			paramMap.put("branch_nm", tempMap.get("branch_nm"));
			paramMap.put("sell_dt", tempMap.get("sell_dt"));
			paramMap.put("sell_quan", tempMap.get("sell_quan"));
			paramMap.put("return_seq", tempMap.get("return_seq"));
			
			paramMap.put("sell_seq", tempMap.get("sell_seq"));
			paramMap.put("prod_no", tempMap.get("prod_no"));
			paramMap.put("unit", tempMap.get("unit"));
			
    		//상품 등록여부 조회
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//상품 등록여부 조회 후 없으면 저장
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//있으면 과세여부, 상품종류 수정
    			sellMapper.setProductInfoForList(paramMap);
    		}
    		
    		//sell 정보 변경함. 미입고로 인한 삭제는 판매 메뉴에 가서 하세요!
    		sellMapper.setUpdSellForForm(paramMap);
    		
    		//해당 판매상품의 마지막 단가입력일자
    		sMaxPriceStartDt = receiptMapper.getMaxPriceStartDt(paramMap);
    		
    		//입력한 단가
    		sInputPrice = paramMap.get("prod_price").toString();

    		//단가 처리
    		if(null != sInputPrice && !"".equals(sInputPrice)) {
    			if(null == sMaxPriceStartDt || "".equals(sMaxPriceStartDt) || Integer.parseInt(sMaxPriceStartDt) <= Integer.parseInt(paramMap.get("sell_dt").toString())) {
		            priceInfo = receiptMapper.getProductPriceInfo(paramMap);
		            if(null != priceInfo && null != priceInfo.get("prod_price")){
	
		            	//조회된 단가
		            	sGetPrice = priceInfo.get("prod_price").toString();
		            	
		            	//단가가 있는데 금액이 다르다면  종료일을 업데이트하고 새로운 단가를 생성
		            	if(!sInputPrice.equals(sGetPrice)) {
		            		if(paramMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {
		            			//sell_dt가 동일하면 가격만 업데이트
		            			paramMap.put("prod_no", priceInfo.get("prod_no"));
			            		paramMap.put("prod_seq", priceInfo.get("prod_seq"));
			            		paramMap.put("unit", priceInfo.get("unit"));
		            			receiptMapper.setUpdProductPrice(paramMap);
		            		} else {
			            		paramMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
			            		paramMap.put("cust_no", priceInfo.get("cust_no"));
			            		paramMap.put("prod_no", priceInfo.get("prod_no"));
			            		paramMap.put("prod_seq", priceInfo.get("prod_seq"));
			            		paramMap.put("unit", priceInfo.get("unit"));
			            		receiptMapper.setProductPriceEndDt(paramMap);	//종료일 업뎃
				            	
				            	receiptMapper.setProductPrice(paramMap);		//새로운 단가 생성
		            		}
		            	} 
		            } else {            	
		            	//없다면 새로 생성
		            	paramMap.put("next_prod_seq", "1");
		            	receiptMapper.setProductPrice(paramMap);
		            }
    			} else {
    				//과거의 단가를 수정할때!
    				
    				//단가의 입력 상태를 확인함
    				priceDate = receiptMapper.getProductPriceDate(paramMap);
    				if(priceDate != null) {
	    				if(paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					//start_dt와 end_dt가 같아 단가만 upate 실행
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setUpdProductPrice(priceDate);
	    					
	    				} else if(paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& !paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					
	    					//기존 row를 복사해서 start_dt를 sell_dt +1 나머지는 그대로 insert해주고 
	    					sTmpDt = paramMap.get("sell_dt").toString();
	    					date = df.parse(sTmpDt);
	    			        cal.setTime(date);
	    			        cal.add(Calendar.DATE, 1);    			            			        
	    					priceDate.put("start_dt", df.format(cal.getTime()));
	    					receiptMapper.setProductPriceDate(priceDate);
	    					
	    					//기존데이터의 입력한 날짜의 단가와 end_dt를 수정한다.
	    					priceDate.put("sell_dt", sTmpDt);
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setProductPriceEndDtStartDt(priceDate);
	    					receiptMapper.setUpdProductPrice(priceDate);
	    					
	    				} else if(!paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					//기존 row를 복사해서 start_dt와 end_dt를 sell_dt로  단가는 입력된 대로 나머지는 그대로 insert해주고    	
	    					priceDate.put("start_dt", paramMap.get("sell_dt"));
	    					priceDate.put("end_dt", paramMap.get("sell_dt"));
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setProductPriceDate(priceDate);
	    					
	    					//기존데이터의 입력한 날짜의 end_dt를 sell_dt - 1로  update한다.
	    					priceDate.put("sell_dt", paramMap.get("sell_dt"));
	    					receiptMapper.setProductPriceEndDt(priceDate);
	    				} else if(!paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& !paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) { 
	    					//수정하려는 sell_dt가 포함된 row 즉 start_dt ~ end_dt를 각각 row로 생성한다.
	    					sOrgPrice = priceDate.get("prod_price").toString();
	    					
	    					receiptMapper.setProductPriceEndDtStartDt(priceDate);
	    					
	    					iDfDt = Integer.parseInt(priceDate.get("df_dt").toString());
	    					sTmpDt = priceDate.get("start_dt").toString();
	    					for(int k=1; k <= iDfDt; k++) {
	    						date = df.parse(sTmpDt);
		    			        cal.setTime(date);
		    			        cal.add(Calendar.DATE, k);    			            			        
		    					priceDate.put("start_dt", df.format(cal.getTime()));
		    					priceDate.put("end_dt", df.format(cal.getTime()));
		    					
		    					if(paramMap.get("sell_dt").equals(priceDate.get("start_dt"))) {
		    						priceDate.put("prod_price", sInputPrice);
		    					} else {
		    						priceDate.put("prod_price", sOrgPrice);
		    					}
		    					receiptMapper.setProductPriceDate(priceDate);	    
	    					}

	    				}
    				} else {
    					if(sMaxPriceStartDt != null && !"".equals(sMaxPriceStartDt)) {
    						paramMap.put("start_dt", paramMap.get("sell_dt").toString());
    						paramMap.put("end_dt", paramMap.get("sell_dt").toString());
	    					receiptMapper.setProductPriceDate(paramMap);
    					} else {
	    					//없다면 새로 생성
			            	paramMap.put("next_prod_seq", "1");
			            	receiptMapper.setProductPrice(paramMap);
    					}
    				}
    			}
    		}
		}
    	return rtnMap;
    }
	
	/**
	 * 영수증 다운로드전 미입고 내역 조회
	 * @since 2018.02.19
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getCheckSellType(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getCheckSellType(map);		
    }
	
	/**
	 * 영수증 하단의 수금 금액 합계
	 * @since 2018.04.14
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getCollMoneyType2Amt(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getCollMoneyType2Amt(map);		
    }
}
