package com.hk.gs.product.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ProductMapper {
	
	public int setInsProductForForm(Map<String, Object> map) throws Exception;
	public int setUpdProductForForm(Map<String, Object> map) throws Exception;
	public Map<String, Object> getProductInfo(Map<String, Object> map) throws Exception;	
	public List<HashMap<String, Object>> getProductList(Map<String, Object> map) throws Exception;
}
