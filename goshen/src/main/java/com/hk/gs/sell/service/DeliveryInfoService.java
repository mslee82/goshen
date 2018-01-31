package com.hk.gs.sell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.sell.mapper.DeliveryInfoMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("deliveryInfoService")
public class DeliveryInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryInfoService.class);
	
	@Resource(name="deliveryInfoMapper")
    private DeliveryInfoMapper deliveryInfoMapper;
	
	/**
	 * ��۱��� ������� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getDeliveryListForDrivers(Map<String, Object> map) throws Exception {
    	return deliveryInfoMapper.getDeliveryListForDrivers(map);		
    }	
	
	/**
	 * ��� �⺻����
	 * @since 2017.12.18
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getTruckInfo(Map<String, Object> map) throws Exception {		
    	return deliveryInfoMapper.getTruckInfo(map);		
    }
}
