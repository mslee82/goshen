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
	 * �ܰ�ǥ ���
	 * @since 2017.12.06
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> productPriceList(Map<String, Object> map) throws Exception {
		
    	return productPriceMapper.getProductPriceList(map);		
    }
	
	/**
	 * �ܰ�ǥ ����
	 * @since 2017.12.17
	 * @author �̸�
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
    		
    		//������ ���� ���� max prod_seq end_dt �츮��
    		productPriceMapper.setUpdProductPriceEndDtRollBack(map);    		
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
	/**
	 * �ܰ�ǥ ����
	 * @since 2017.12.17
	 * @author �̸�
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
	 * ���� �� ���� �ܰ�ǥ ���
	 * @since 2018.03.04
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> comProductPriceList(Map<String, Object> map) throws Exception {		
    	return productPriceMapper.getComProductPriceList(map);		
    }	
	
	/**
	 * ���� �� ���� �ܰ�ǥ  insert
	 * @since 2018.03.05
	 * @author �̸�
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

            //������ ����ִ� �ܰ�ǥ�� �ִٸ� �������� ������Ʈ�ϰ� ���ο� �ܰ��� �����Ѵ�.
			if(null != tempMap.get("sell_prod_price") && !"".equals(tempMap.get("sell_prod_price"))) {
	            priceInfo = productPriceMapper.getComProductPriceInfo(tempMap);
	            if(null != priceInfo && null != priceInfo.get("prod_no")) {
	            	
	            	//�ܰ��� �ִµ� �ݾ��� �ٸ��ٸ�  �������� ������Ʈ�ϰ� ���ο� �ܰ��� ����
	            	if(!tempMap.get("sell_prod_price").equals(priceInfo.get("prod_price")) ) {
	            		
	            		tempParamMap.put("prod_no", priceInfo.get("prod_no"));
		            	tempParamMap.put("prod_seq", priceInfo.get("prod_seq"));
		            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
		            	tempParamMap.put("prod_price", tempMap.get("sell_prod_price"));
		            	tempParamMap.put("unit", priceInfo.get("unit"));
		            	tempParamMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
		            	
	            		if(tempParamMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {
	            			//sell_dt�� �����ϸ� ���ݸ� ������Ʈ
	            			productPriceMapper.setUpdComProductPrice(tempParamMap);
	            		} else {
			            	
	            			//���� ������ ������ ����
			            	productPriceMapper.setComProductPriceEndDt(tempParamMap);
			            	
			            	//����ܰ� ����
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
	            	
	            	//����ܰ� ����
	                productPriceMapper.setComProductPrice(tempParamMap);
	            }
			}
            
            //������ ����ִ� ���� �ܰ�ǥ�� �ִٸ� �������� ������Ʈ�ϰ� ���ο� �ܰ��� �����Ѵ�.
            if(null != tempMap.get("buy_prod_price") && !"".equals(tempMap.get("buy_prod_price"))) {
            	
	            priceInfo = productPriceMapper.getBuyProductPriceInfo(tempMap);
	            if(null != priceInfo && null != priceInfo.get("prod_no")) {
	            	
	            	//�ܰ��� �ִµ� �ݾ��� �ٸ��ٸ�  �������� ������Ʈ�ϰ� ���ο� �ܰ��� ����
	            	if(!tempMap.get("buy_prod_price").equals(priceInfo.get("prod_price")) ) {
		            	tempParamMap.put("prod_no", priceInfo.get("prod_no"));
		            	tempParamMap.put("prod_seq", priceInfo.get("prod_seq"));
		            	tempParamMap.put("sell_dt", tempMap.get("sell_dt"));
		            	tempParamMap.put("prod_price", tempMap.get("buy_prod_price"));
		            	tempParamMap.put("unit", priceInfo.get("unit"));
		            	tempParamMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
		            	
		            	if(tempParamMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {

		            		//sell_dt�� �����ϸ� ���ݸ� ������Ʈ
		            		productPriceMapper.setUpdBuyProductPrice(tempParamMap);
		            	} else {
		            		
		            		//���� ������ ������ ����
		            		productPriceMapper.setBuyProductPriceEndDt(tempParamMap);
		            		
		            		//���Ŵܰ� ����
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
	            	
	            	//���Ŵܰ� ����
	            	productPriceMapper.setBuyProductPrice(tempParamMap);
	            }
            }
        }

        rtnMap.put("returnYn", "Y");
    	return rtnMap;
    }
}
