package com.hk.gs.receipt.service;

import java.util.ArrayList;
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
	public List<HashMap<String, Object>> getSellDtListForBranch(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtListForBranch(map);		
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
		int iProdCnt = 0;
		String sInputPrice = "";
		String sGetPrice = "";
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
			//paramMap.put("sell_seq", tempMap.get("sell_seq"));
			paramMap.put("sell_dt", tempMap.get("sell_dt"));
			paramMap.put("sell_quan", tempMap.get("sell_quan"));
			paramMap.put("return_seq", tempMap.get("return_seq"));
			
    		//상품 등록여부 조회
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//상품 등록여부 조회 후 없으면 저장
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//있으면 과세여부, 상품종류 수정
    			sellMapper.setProductInfoForList(paramMap);
    		}
    		sInputPrice = paramMap.get("prod_price").toString();
    		
    		//단가 처리
    		if(null != sInputPrice && !"".equals(sInputPrice)) {
	            priceInfo = receiptMapper.getProductPriceInfo(paramMap);
	            if(null != priceInfo && null != priceInfo.get("prod_price")){
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
}
