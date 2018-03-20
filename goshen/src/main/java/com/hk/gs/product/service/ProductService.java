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
import com.hk.gs.util.CommUtil;

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
	 * 상품 내역 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getProductInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = productMapper.getProductInfo(map);
    	return rtnMap;
    }
	
	/**
	 * 상품 입력폼을 이용한 수정 및 등록
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public int setProductForForm(Map<String, Object> map) throws Exception {    		
		int iRst = 0;
		//merge 사용불가. sell_seq 때문에 duplicate 발생 안함.
		//수정 혹은 등록
		if("I".equals(map.get("mode"))){
			iRst = productMapper.setInsProductForForm(map);
		} else {
			iRst = productMapper.setUpdProductForForm(map);
		}
    	return iRst;
    }
	
	/**
	 * 상품 목록 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getProductList(Map<String, Object> map) throws Exception {		
    	return productMapper.getProductList(map);		
    }
	
	/**
	 * 상품 목록에서 수정
	 * @since 2018.03.20
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setProductList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> tempMap;

		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			
			//상품명 " 기호 replace
			tempMap.put("prod_nm", tempMap.get("prod_nm").toString().replaceAll("&quot;", "\""));
			productMapper.setUpdProductForForm(tempMap);
		}

	    rtnMap.put("returnYn", "Y");
    	return rtnMap;
    }
}
