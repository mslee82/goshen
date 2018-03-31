package com.hk.gs.product.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ProductPriceMapper {
	
	public List<HashMap<String, Object>> getProductPriceList(Map<String, Object> map) throws Exception;
	public int setDelProductPrice(Map<String, Object> map) throws Exception;
	public int setUpdProductPriceEndDtRollBack(Map<String, Object> map) throws Exception;
	public int setUpdProductPriceInfo(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getComProductPriceList(Map<String, Object> map) throws Exception;
	public Map<String, Object> getComProductPriceInfo(Map<String, Object> map) throws Exception;
	public int setComProductPriceEndDt(Map<String, Object> map) throws Exception;
	public int setComProductPrice(Map<String, Object> map) throws Exception;
	public int setUpdComProductPrice(Map<String, Object> map) throws Exception;
	public int setComProductPriceEndDtRollBack(Map<String, Object> map) throws Exception;
	
	public int setBuyProductPrice(Map<String, Object> map) throws Exception;
	public int setUpdBuyProductPrice(Map<String, Object> map) throws Exception;
	public int setBuyProductPriceEndDt(Map<String, Object> map) throws Exception;
	public int setBuyProductPriceEndDtRollBack(Map<String, Object> map) throws Exception;
	public Map<String, Object> getBuyProductPriceInfo(Map<String, Object> map) throws Exception;
	
}
