package com.hk.gs.sell.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface DeliveryInfoMapper {
	
	public List<HashMap<String, Object>> getDeliveryListForDrivers(Map<String, Object> map) throws Exception;
	public Map<String, Object> getTruckInfo(Map<String, Object> map) throws Exception;
}
