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
	public List<HashMap<String, Object>> getSellDtListLv3(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtListLv3(map);		
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
			
    		//��ǰ ��Ͽ��� ��ȸ
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//��ǰ ��Ͽ��� ��ȸ �� ������ ����
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//������ ��������, ��ǰ���� ����
    			sellMapper.setProductInfoForList(paramMap);
    		}
    		
    		//sell ���� ������. ���԰�� ���� ������ �Ǹ� �޴��� ���� �ϼ���!
    		sellMapper.setUpdSellForForm(paramMap);
    		
    		//�ش� �ǸŻ�ǰ�� ������ �ܰ��Է�����
    		sMaxPriceStartDt = receiptMapper.getMaxPriceStartDt(paramMap);
    		
    		//�Է��� �ܰ�
    		sInputPrice = paramMap.get("prod_price").toString();

    		//�ܰ� ó��
    		if(null != sInputPrice && !"".equals(sInputPrice)) {
    			if(null == sMaxPriceStartDt || "".equals(sMaxPriceStartDt) || Integer.parseInt(sMaxPriceStartDt) <= Integer.parseInt(paramMap.get("sell_dt").toString())) {
		            priceInfo = receiptMapper.getProductPriceInfo(paramMap);
		            if(null != priceInfo && null != priceInfo.get("prod_price")){
	
		            	//��ȸ�� �ܰ�
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
    			} else {
    				//������ �ܰ��� �����Ҷ�!
    				
    				//�ܰ��� �Է� ���¸� Ȯ����
    				priceDate = receiptMapper.getProductPriceDate(paramMap);
    				if(priceDate != null) {
	    				if(paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					//start_dt�� end_dt�� ���� �ܰ��� upate ����
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setUpdProductPrice(priceDate);
	    					
	    				} else if(paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& !paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					
	    					//���� row�� �����ؼ� start_dt�� sell_dt +1 �������� �״�� insert���ְ� 
	    					sTmpDt = paramMap.get("sell_dt").toString();
	    					date = df.parse(sTmpDt);
	    			        cal.setTime(date);
	    			        cal.add(Calendar.DATE, 1);    			            			        
	    					priceDate.put("start_dt", df.format(cal.getTime()));
	    					receiptMapper.setProductPriceDate(priceDate);
	    					
	    					//������������ �Է��� ��¥�� �ܰ��� end_dt�� �����Ѵ�.
	    					priceDate.put("sell_dt", sTmpDt);
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setProductPriceEndDtStartDt(priceDate);
	    					receiptMapper.setUpdProductPrice(priceDate);
	    					
	    				} else if(!paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) {
	    					//���� row�� �����ؼ� start_dt�� end_dt�� sell_dt��  �ܰ��� �Էµ� ��� �������� �״�� insert���ְ�    	
	    					priceDate.put("start_dt", paramMap.get("sell_dt"));
	    					priceDate.put("end_dt", paramMap.get("sell_dt"));
	    					priceDate.put("prod_price", sInputPrice);
	    					receiptMapper.setProductPriceDate(priceDate);
	    					
	    					//������������ �Է��� ��¥�� end_dt�� sell_dt - 1��  update�Ѵ�.
	    					priceDate.put("sell_dt", paramMap.get("sell_dt"));
	    					receiptMapper.setProductPriceEndDt(priceDate);
	    				} else if(!paramMap.get("sell_dt").equals(priceDate.get("start_dt"))
	    						&& !paramMap.get("sell_dt").equals(priceDate.get("end_dt"))) { 
	    					//�����Ϸ��� sell_dt�� ���Ե� row �� start_dt ~ end_dt�� ���� row�� �����Ѵ�.
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
	    					//���ٸ� ���� ����
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
	 * ������ �ٿ�ε��� ���԰� ���� ��ȸ
	 * @since 2018.02.19
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getCheckSellType(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getCheckSellType(map);		
    }
	
	/**
	 * ������ �ϴ��� ���� �ݾ� �հ�
	 * @since 2018.04.14
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getCollMoneyType2Amt(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getCollMoneyType2Amt(map);		
    }
}
