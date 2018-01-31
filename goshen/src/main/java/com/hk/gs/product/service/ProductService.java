package com.hk.gs.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.product.mapper.ProductMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("productService")
public class ProductService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Resource(name="productMapper")
    private ProductMapper productMapper;

	
	/**
	 * ��ǰ ���� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getProductInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = productMapper.getProductInfo(map);
    	return rtnMap;
    }
	
	/**
	 * ��ǰ �Է����� �̿��� ���� �� ���
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public int setProductForForm(Map<String, Object> map) throws Exception {    		
		int iRst = 0;
		//merge ���Ұ�. sell_seq ������ duplicate �߻� ����.
		//���� Ȥ�� ���
		if("I".equals(map.get("mode"))){
			iRst = productMapper.setInsProductForForm(map);
		} else {
			iRst = productMapper.setUpdProductForForm(map);
		}
    	return iRst;
    }
	
	/**
	 * ��ǰ ��� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getProductList(Map<String, Object> map) throws Exception {		
    	return productMapper.getProductList(map);		
    }
}
