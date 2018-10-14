package com.hk.gs.receipt.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReceiptMapper {
	
	public List<HashMap<String, Object>> getReceiptList(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCustInfo(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellDtList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellDtListLv3(Map<String, Object> map) throws Exception;
	public Map<String, Object> getSellTotalPrice(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCheckSellType(Map<String, Object> map) throws Exception;
	public int setProductPrice(Map<String, Object> map) throws Exception;
	public int setProductPriceEndDt(Map<String, Object> map) throws Exception;
	public int setUpdProductPrice(Map<String, Object> map) throws Exception;
	public Map<String, Object> getProductPriceInfo(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCollMoneyType2Amt(Map<String, Object> map) throws Exception;
	public String getMaxPriceStartDt(Map<String, Object> map) throws Exception;
	public Map<String, Object> getProductPriceDate(Map<String, Object> map) throws Exception;
	public int setProductPriceStartDt(Map<String, Object> map) throws Exception;
	public int setProductPriceDate(Map<String, Object> map) throws Exception;
	public int setProductPriceEndDtStartDt(Map<String, Object> map) throws Exception;
}
