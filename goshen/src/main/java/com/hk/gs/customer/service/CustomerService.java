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
	 * �� ���� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getCustomerInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = customerMapper.getCustomerInfo(map);
    	return rtnMap;
    }
	
	/**
	 * �� �Է����� �̿��� ���� �� ���
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> setCustomerForForm(Map<String, Object> map) throws Exception {    		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iRst = 0;
		//merge ���Ұ�. sell_seq ������ duplicate �߻� ����.
		//���� Ȥ�� ���
		if("I".equals(map.get("mode"))){
			iRst = customerMapper.setInsCustomerForForm(map);
		} else {
			iRst = customerMapper.setUpdCustomerForForm(map);
		}
		rtnMap.put("rtn", iRst);
    	return rtnMap;
    }
	
	/**
	 * �� ��� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getCustomerList(Map<String, Object> map) throws Exception {		
    	return customerMapper.getCustomerList(map);		
    }
	
	/**
	 * �� ����
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> delCustomer(Map<String, Object> map) throws Exception {    		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iRst = customerMapper.delCustomer(map);
		rtnMap.put("rtn", iRst);
    	return rtnMap;
    }
}
