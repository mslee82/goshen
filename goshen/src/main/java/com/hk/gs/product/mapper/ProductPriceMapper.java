package com.hk.gs.product.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ProductPriceMapper {
	
	public Map<String, Object> getProductPriceUploadInfo(Map<String, Object> map) throws Exception;
	public int setProductPriceEndDt(Map<String, Object> map) throws Exception;
	public int setProductPriceForExcelUpload(Map<String, Object> map) throws Exception;
	public List<HashMap<String, Object>> getProductPriceList(Map<String, Object> map) throws Exception;
	public int setDelProductPrice(Map<String, Object> map) throws Exception;
	public int setUpdProductPriceEndDtRollBack(Map<String, Object> map) throws Exception;
	public int setUpdProductPriceInfo(Map<String, Object> map) throws Exception;
}
