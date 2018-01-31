package com.hk.gs.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.main.mapper.HomeMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("homeService")
public class HomeService {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeService.class);
	
	@Resource(name = "homeMapper")
    private HomeMapper homeMapper;
	
	/**
	 * 거래처 매출 현황 TOP5
	 * @since 2017.12.27
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> customerSalesTop5(Map<String, Object> map) throws Exception {
    	return homeMapper.customerSalesTop5(map);		
    }
}
