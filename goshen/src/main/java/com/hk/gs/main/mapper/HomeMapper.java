package com.hk.gs.main.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HomeMapper {
	
	/**
	 * �ŷ�ó ���� ��Ȳ TOP5
	 * @since 2017.12.27
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> customerSalesTop5(Map<String, Object> map) throws Exception;
	
}
