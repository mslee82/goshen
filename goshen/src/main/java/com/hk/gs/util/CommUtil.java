package com.hk.gs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommUtil {
	
	private static final Logger log = Logger.getLogger(CommUtil.class);	
	
	/**
	 * json�� List�� ��ȯ
	 * @since 2017.12.13
	 * @author �̸�
	 */
	public static List<Map<String, Object>> json2List(String jsonData) throws Exception {
		log.debug("jsonData : " + jsonData);
		
		byte[] mapData = jsonData.getBytes("UTF-8");
		
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			lists = objMapper.readValue(mapData,  new TypeReference<List<Map<String, Object>>>() {});			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * json�� Map���� ��ȯ
	 * @since 2017.12.13
	 * @author �̸�
	 */
	public static Map<String, Object> json2Map(String jsonData) throws Exception {
		log.debug("jsonData : " + jsonData);
		byte[] mapData = jsonData.getBytes("UTF-8");
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			map = objMapper.readValue(mapData,  new TypeReference<Map<String, Object>>() {});			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Map�� json���� ��ȯ
	 * @since 2017.12.13
	 * @author �̸�
	 */
	public static String map2Json(Map<String, Object> dataMap) throws Exception {
		String jsonData = null;
		
		ObjectMapper objMapper = new ObjectMapper();
		try {
			jsonData = objMapper.writeValueAsString(dataMap);			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	/**
	 * List�� json���� ��ȯ
	 * @since 2017.12.13
	 * @author �̸�
	 */
	public static String list2Json(List<Object> dataList) throws Exception {
		String jsonData = null;
		
		ObjectMapper objMapper = new ObjectMapper();
		try {
			jsonData = objMapper.writeValueAsString(dataList);			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonData;
	}
	
}
