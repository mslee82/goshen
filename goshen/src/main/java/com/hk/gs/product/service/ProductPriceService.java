package com.hk.gs.product.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hk.gs.product.mapper.ProductPriceMapper;
import com.hk.gs.util.CommUtil;
import com.hk.gs.util.ExcelRead;
import com.hk.gs.util.ExcelReadOption;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("productPriceService")
public class ProductPriceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);
	
	@Resource(name="productPriceMapper")
    private ProductPriceMapper productPriceMapper;

	/**
	 * 단가표 업로드해 product_price table에  insert
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	public int setProductPriceForExcelUpload(MultipartFile excelFile) throws Exception {
		if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }
	        
	    File destFile = new File("D:\\"+excelFile.getOriginalFilename());
	    try {
	    	excelFile.transferTo(destFile);
	    } catch(IOException e) {
	    	throw new RuntimeException(e.getMessage(),e);
	    }
	        
        //Service 단에서 가져온 코드 
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setOutputColumns("A","B","C","D","E","F");
        excelReadOption.setStartRow(1);        
	        
        List<Map<String, Object>>excelContent = ExcelRead.read(excelReadOption);
        Map<String, Object> priceInfo = null;
        for(Map<String, Object> article: excelContent){
            System.out.println(article.get("A"));
            System.out.println(article.get("B"));
            System.out.println(article.get("C"));
            System.out.println(article.get("D"));
            System.out.println(article.get("E"));
            System.out.println(article.get("F"));
            //기존에 살아있는 단가표가 있다면 종료일을 업데이트하고 새로운 단가를 생성한다.
            priceInfo = productPriceMapper.getProductPriceUploadInfo(article);
            if(null != priceInfo && null != priceInfo.get("cust_no")) {
            	article.put("cust_no", priceInfo.get("cust_no"));
            	article.put("prod_no", priceInfo.get("prod_no"));
            	article.put("prod_seq", priceInfo.get("prod_seq"));
            	article.put("next_prod_seq", priceInfo.get("next_prod_seq"));
            	productPriceMapper.setProductPriceEndDt(article);
            } else {
            	article.put("next_prod_seq", "1");
            }
            productPriceMapper.setProductPriceForExcelUpload(article);
        }

    	return 1;
    }
	
	/**
	 * 단가표 목록
	 * @since 2017.12.06
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> productPriceList(Map<String, Object> map) throws Exception {
		
    	return productPriceMapper.getProductPriceList(map);		
    }
	
	/**
	 * 단가표 삭제
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setDelProductPrice(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("cust_no");
			map.remove("prod_no");
			map.remove("prod_seq");
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("prod_no", tempMap.get("prod_no"));
    		map.put("prod_seq", tempMap.get("prod_seq"));
    		
    		iRst = productPriceMapper.setDelProductPrice(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("prod_seq"));
    		}
    		
    		//삭제로 인한 현재 max prod_seq end_dt 살리기
    		productPriceMapper.setUpdProductPriceEndDtRollBack(map);    		
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
	/**
	 * 단가표 수정
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setUpdProductPriceInfo(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("cust_no");
			map.remove("prod_no");
			map.remove("prod_seq");
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("prod_no", tempMap.get("prod_no"));
    		map.put("prod_seq", tempMap.get("prod_seq"));
    		
    		iRst = productPriceMapper.setUpdProductPriceInfo(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("prod_seq"));
    		}    			
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
}
