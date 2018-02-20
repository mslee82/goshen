package com.hk.gs.receipt.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReceiptMapper {
	
	public List<HashMap<String, Object>> getReceiptList(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCustInfo(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellDtList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellDtListForBranch(Map<String, Object> map) throws Exception;
	public Map<String, Object> getSellTotalPrice(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCheckSellType(Map<String, Object> map) throws Exception;
}
