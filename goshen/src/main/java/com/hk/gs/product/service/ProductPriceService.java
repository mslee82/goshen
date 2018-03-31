package com.hk.gs.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.product.mapper.ProductPriceMapper;
import com.hk.gs.util.CommUtil;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("productPriceService")
public class ProductPriceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);
	
	@Resource(name="productPriceMapper")
    private ProductPriceMapper productPriceMapper;
	
	/**
	 * 단가표 목록
	 * @since 2017.12.06
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> productPriceList(Map<String, Object> map) throws Exception {
		
    	return productPriceMapper.getProductPriceList(map);		
    }
	
	/**
	 * 단가표 삭제
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setDelProductPrice(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("cust_no");
			map.remove("prod_no");
			map.remove("prod_seq");
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("prod_no", tempMap.get("prod_no"));
    		map.put("prod_seq", tempMap.get("prod_seq"));
    		
    		iRst = productPriceMapper.setDelProductPrice(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("prod_seq"));
    		}
    		
    		//삭제로 인한 현재 max prod_seq end_dt 살리기
    		productPriceMapper.setUpdProductPriceEndDtRollBack(map);    		
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
	/**
	 * 단가표 수정
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setUpdProductPriceInfo(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("cust_no");
			map.remove("prod_no");
			map.remove("prod_seq");
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("prod_no", tempMap.get("prod_no"));
    		map.put("prod_seq", tempMap.get("prod_seq"));
    		
    		iRst = productPriceMapper.setUpdProductPriceInfo(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("prod_seq"));
    		}    			
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
	/**
	 * 공통 및 구매 단가표 목록
	 * @since 2018.03.04
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> comProductPriceList(Map<String, Object> map) throws Exception {		
    	return productPriceMapper.getComProductPriceList(map);		
    }	
	
	/**
	 * 공통 및 구매 단가표  insert
	 * @since 2018.03.05
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setComProductPrice(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> priceInfo = null;
        Map<String, Object> tempMap;
        Map<String, Object> tempParamMap;
        for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			tempParamMap = new HashMap<String, Object>();

            //기존에 살아있는 단가표가 있다면 종료일을 업데이트하고 새로운 단가를 생성한다.
			if(null != tempMap.get("sell_prod_price") && !"".equals(tempMap.get("sell_prod_price"))) {
	            priceInfo = productPriceMapper.getComProductPriceInfo(tempMap);
	            if(null != priceInfo && null != priceInfo.get("prod_no")) {
	            	
	            	//단가가 있는데 금액이 다르다면  종료일을 업데이트하고 새로운 단가를 생성
	            	if(!tempMap.get("sell_prod_price").equals(priceInfo.get("prod_price")) ) {
	            		
	            		tempParamMap.put("prod_no", priceInfo.get("prod_no"));
		            	tempParamMap.put("prod_seq", priceInfo.get("prod_seq"));
		            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
		            	tempParamMap.put("prod_price", tempMap.get("sell_prod_price"));
		            	tempParamMap.put("unit", priceInfo.get("unit"));
		            	tempParamMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
		            	
	            		if(tempParamMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {
	            			//sell_dt가 동일하면 가격만 업데이트
	            			productPriceMapper.setUpdComProductPrice(tempParamMap);
	            		} else {
			            	
	            			//기존 데이터 종료일 업뎃
			            	productPriceMapper.setComProductPriceEndDt(tempParamMap);
			            	
			            	//공통단가 생성
			                productPriceMapper.setComProductPrice(tempParamMap);
	            		}
	            	}
	            } else {
	            	tempParamMap.put("prod_no", tempMap.get("prod_no"));
	            	tempParamMap.put("prod_seq", tempMap.get("prod_seq"));
	            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
	            	tempParamMap.put("prod_price", tempMap.get("sell_prod_price"));
	            	tempParamMap.put("unit", tempMap.get("unit"));
	            	tempParamMap.put("next_prod_seq", "1");
	            	
	            	//공통단가 생성
	                productPriceMapper.setComProductPrice(tempParamMap);
	            }
			}
            
            //기존에 살아있는 구매 단가표가 있다면 종료일을 업데이트하고 새로운 단가를 생성한다.
            if(null != tempMap.get("buy_prod_price") && !"".equals(tempMap.get("buy_prod_price"))) {
            	
	            priceInfo = productPriceMapper.getBuyProductPriceInfo(tempMap);
	            if(null != priceInfo && null != priceInfo.get("prod_no")) {
	            	
	            	//단가가 있는데 금액이 다르다면  종료일을 업데이트하고 새로운 단가를 생성
	            	if(!tempMap.get("buy_prod_price").equals(priceInfo.get("prod_price")) ) {
		            	tempParamMap.put("prod_no", priceInfo.get("prod_no"));
		            	tempParamMap.put("prod_seq", priceInfo.get("prod_seq"));
		            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
		            	tempParamMap.put("prod_price", tempMap.get("buy_prod_price"));
		            	tempParamMap.put("unit", priceInfo.get("unit"));
		            	tempParamMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
		            	
		            	if(tempParamMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {

		            		//sell_dt가 동일하면 가격만 업데이트
		            		productPriceMapper.setUpdBuyProductPrice(tempParamMap);
		            	} else {
		            		
		            		//기존 데이터 종료일 업뎃
		            		productPriceMapper.setBuyProductPriceEndDt(tempParamMap);
		            		
		            		//구매단가 생성
		            		productPriceMapper.setBuyProductPrice(tempParamMap);
		            	}
		            	
	            	}
	            } else {
	            	tempParamMap.put("prod_no", tempMap.get("prod_no"));
	            	tempParamMap.put("prod_seq", tempMap.get("prod_seq"));
	            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
	            	tempParamMap.put("prod_price", tempMap.get("buy_prod_price"));
	            	tempParamMap.put("unit", tempMap.get("unit"));
	            	tempParamMap.put("next_prod_seq", "1");
	            	
	            	//구매단가 생성
	            	productPriceMapper.setBuyProductPrice(tempParamMap);
	            }
            }
        }

        rtnMap.put("returnYn", "Y");
    	return rtnMap;
    }
}
