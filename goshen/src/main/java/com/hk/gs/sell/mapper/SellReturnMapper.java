package com.hk.gs.sell.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface SellReturnMapper {
	
	public int setSellReturn(Map<String, Object> map) throws Exception;
	public int setUpdSellReturn(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getSellReturnList(Map<String, Object> map) throws Exception;
	public int setDelSellReturn(Map<String, Object> map) throws Exception;
}
