package com.hk.gs.main.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HomeMapper {
	
	/**
	 * 거래처 매출 현황 TOP5
	 * @since 2017.12.27
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> customerSalesTop5(Map<String, Object> map) throws Exception;
	
}
