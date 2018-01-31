package com.hk.gs.common.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ComMapper {
	
	public List<HashMap<String, Object>> getCustNmList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getProdNmList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getUnitNmList(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getTruckNmList(Map<String, Object> map) throws Exception;
}
