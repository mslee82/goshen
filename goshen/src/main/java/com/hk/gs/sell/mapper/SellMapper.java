package com.hk.gs.sell.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface SellMapper {
	
	public int setSellForExcelUpload(Map<String, Object> map) throws Exception;
	public int setInsSellForForm(Map<String, Object> map) throws Exception;
	public int setUpdSellForForm(Map<String, Object> map) throws Exception;
	public int setUpdSellForList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellList(Map<String, Object> map) throws Exception;
	public Map<String, Object> getSellInfo(Map<String, Object> map) throws Exception;	
	public Map<String, Object> getSellUploadExePrev(Map<String, Object> map) throws Exception;
	public int getProductInfo(Map<String, Object> map) throws Exception;	
	public int setProductForList(Map<String, Object> map) throws Exception;
	public int setProductInfoForList(Map<String, Object> map) throws Exception;			
	public int delSellList(Map<String, Object> map) throws Exception;
}
