package com.hk.gs.customer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.customer.mapper.CustomerMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("customerService")
public class CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
	
	@Resource(name="customerMapper")
    private CustomerMapper customerMapper;

	
	/**
	 * 고객 정보 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getCustomerInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = customerMapper.getCustomerInfo(map);
    	return rtnMap;
    }
	
	/**
	 * 고객 입력폼을 이용한 수정 및 등록
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setCustomerForForm(Map<String, Object> map) throws Exception {    		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iRst = 0;
		//merge 사용불가. sell_seq 때문에 duplicate 발생 안함.
		//수정 혹은 등록
		if("I".equals(map.get("mode"))){
			iRst = customerMapper.setInsCustomerForForm(map);
		} else {
			iRst = customerMapper.setUpdCustomerForForm(map);
		}
		rtnMap.put("rtn", iRst);
    	return rtnMap;
    }
	
	/**
	 * 고객 목록 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getCustomerList(Map<String, Object> map) throws Exception {		
    	return customerMapper.getCustomerList(map);		
    }
	
	/**
	 * 고객 삭제
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> delCustomer(Map<String, Object> map) throws Exception {    		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iRst = customerMapper.delCustomer(map);
		rtnMap.put("rtn", iRst);
    	return rtnMap;
    }
}
