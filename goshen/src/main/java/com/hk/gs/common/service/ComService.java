package com.hk.gs.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.common.mapper.ComMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("comService")
public class ComService {
	
	private static final Logger logger = LoggerFactory.getLogger(ComService.class);
	
	@Resource(name="comMapper")
    private ComMapper comMapper;
	
	/**
	 * ���� ��� ��ȸ
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getCustNmList(Map<String, Object> map) throws Exception {		
    	return comMapper.getCustNmList(map);		
    }
	
	/**
	 * ��ǰ ��� ��ȸ
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getProdNmList(Map<String, Object> map) throws Exception {		
    	return comMapper.getProdNmList(map);		
    }
	
	/**
	 * ���� ��� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getUnitNmList(Map<String, Object> map) throws Exception {		
    	return comMapper.getUnitNmList(map);		
    }
	
	/**
	 * ���� ��� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getTruckNmList(Map<String, Object> map) throws Exception {		
    	return comMapper.getTruckNmList(map);		
    }
}
