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
	 * ������ ������ ��ȸ
	 * @since 2017.10.22
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> previewReceipt(Map<String, Object> map) throws Exception {
    	return receiptMapper.getReceiptList(map);		
    }
	
	/**
	 * ������ �⺻����
	 * @since 2017.12.10
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getCustInfo(Map<String, Object> map) throws Exception {		
    	return receiptMapper.getCustInfo(map);		
    }
	
	/**
	 * ������ ���� ��Ʈ ���
	 * @since 2017.12.24
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtList(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtList(map);		
    }
	
	/**
	 * ������ ���� ��Ʈ ���
	 * @since 2017.12.24
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtListForBranch(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtListForBranch(map);		
    }
	
	/**
	 * ������ �ϴ��� �ݾ� �հ�
	 * @since 2017.12.24
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getSellTotalPrice(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellTotalPrice(map);		
    }
	
	/**
	 * �ǸŰ��� ��� �� ����
	 * @since 2018.01.21
	 * @author �̸�
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
			
    		//��ǰ ��Ͽ��� ��ȸ
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//��ǰ ��Ͽ��� ��ȸ �� ������ ����
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//������ ��������, ��ǰ���� ����
    			sellMapper.setProductInfoForList(paramMap);
    		}
    		sInputPrice = paramMap.get("prod_price").toString();
    		
    		//�ܰ� ó��
    		if(null != sInputPrice && !"".equals(sInputPrice)) {
	            priceInfo = receiptMapper.getProductPriceInfo(paramMap);
	            if(null != priceInfo && null != priceInfo.get("prod_price")){
	            	sGetPrice = priceInfo.get("prod_price").toString();
	            	
	            	//�ܰ��� �ִµ� �ݾ��� �ٸ��ٸ�  �������� ������Ʈ�ϰ� ���ο� �ܰ��� ����
	            	if(!sInputPrice.equals(sGetPrice)) {
	            		if(paramMap.get("sell_dt").equals(priceInfo.get("start_dt"))) {
	            			//sell_dt�� �����ϸ� ���ݸ� ������Ʈ
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
		            		receiptMapper.setProductPriceEndDt(paramMap);	//������ ����
			            	
			            	receiptMapper.setProductPrice(paramMap);		//���ο� �ܰ� ����
	            		}
	            	} 
	            } else {            	
	            	//���ٸ� ���� ����
	            	paramMap.put("next_prod_seq", "1");
	            	receiptMapper.setProductPrice(paramMap);
	            }
    		}
		}
    	return rtnMap;
    }
	
	/**
	 * ������ �ٿ�ε��� ���԰� ���� ��ȸ
	 * @since 2018.02.19
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getCheckSellType(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getCheckSellType(map);		
    }
}
