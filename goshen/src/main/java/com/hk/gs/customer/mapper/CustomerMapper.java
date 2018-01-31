package com.hk.gs.customer.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface CustomerMapper {
	
	public int setInsCustomerForForm(Map<String, Object> map) throws Exception;
	public int setUpdCustomerForForm(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCustomerInfo(Map<String, Object> map) throws Exception;	
	public List<HashMap<String, Object>> getCustomerList(Map<String, Object> map) throws Exception;
}
