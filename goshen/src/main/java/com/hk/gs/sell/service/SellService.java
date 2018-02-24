package com.hk.gs.sell.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hk.gs.sell.mapper.SellMapper;
import com.hk.gs.util.CommUtil;
import com.hk.gs.util.ExcelRead;
import com.hk.gs.util.ExcelReadOption;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("sellService")
public class SellService {
	
	private static final Logger logger = LoggerFactory.getLogger(SellService.class);
	
	@Resource(name="sellMapper")
    private SellMapper sellMapper;

	/**
	 * 발주서 업로드 미리보기
	 * @since 2018.01.21
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<Map<String, Object>> sellUploadExePrev(MultipartFile excelFile) throws Exception {
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
        
        //A:일자, B:지점, C:순번, D:상품명, E:수량, F:단위, G:지점        
        excelReadOption.setOutputColumns("A","B","C","D","E","F","G");
        excelReadOption.setStartRow(1);        
	        
        List<Map<String, Object>> excelContent = ExcelRead.read(excelReadOption);
        List<Map<String, Object>> returnContent = new ArrayList<Map<String, Object>>();
        
        for(Map<String, Object> article: excelContent){
            returnContent.add(sellMapper.getSellUploadExePrev(article));
        }
    	return returnContent;
    }
	
	/**
	 * 발주서 업로드해 sell table에  insert
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	public int setSellForExcelUpload(MultipartFile excelFile) throws Exception {
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
        excelReadOption.setOutputColumns("A","B","C","D","E","F","G");
        excelReadOption.setStartRow(1);        
	        
        List<Map<String, Object>>excelContent = ExcelRead.read(excelReadOption);
        for(Map<String, Object> article: excelContent){
            System.out.println(article.get("A"));
            System.out.println(article.get("B"));
            System.out.println(article.get("C"));
            System.out.println(article.get("D"));
            System.out.println(article.get("E"));
            System.out.println(article.get("F"));
            System.out.println(article.get("G"));
        }
	        
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("sellList", excelContent);
	        
    	return sellMapper.setSellForExcelUpload(reqMap);
    }
	
	/**
	 * 판매 입력폼을 이용한 수정 및 등록
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	public int setSellForForm(Map<String, Object> map) throws Exception {    		
		int iRst = 0;
		//merge 사용불가. sell_seq 때문에 duplicate 발생 안함.
		//수정 혹은 등록
		if("I".equals(map.get("mode"))){
			iRst = sellMapper.setInsSellForForm(map);
		} else {
			iRst = sellMapper.setUpdSellForForm(map);
		}
    	return iRst;
    }
	
	/**
	 * 판매 내역 조회
	 * @since 2017.12.12
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellList(Map<String, Object> map) throws Exception {		
    	return sellMapper.getSellList(map);		
    }
	
	/**
	 * 판매 내역 조회
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getSellInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = sellMapper.getSellInfo(map);
    	return rtnMap;
    }
	
	/**
	 * 판매가액 등록 및 수정
	 * @since 2018.01.21
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setSellList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> tempMap;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, Object> priceInfo = new HashMap<String, Object>();
		int iProdCnt = 0;
		String sInputPrice = "";
		String sGetPrice = "";
		List<Map<String, Object>> sellist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			sellist.add(tempMap);
			
			paramMap.put("prod_nm", tempMap.get("prod_nm"));
			paramMap.put("prod_typ", tempMap.get("prod_typ"));
			paramMap.put("prod_price", tempMap.get("prod_price"));
			paramMap.put("cust_nm", tempMap.get("cust_nm"));
			paramMap.put("tax_yn", tempMap.get("tax_yn"));
			paramMap.put("sell_dt", tempMap.get("sell_dt"));
			paramMap.put("branch_nm", tempMap.get("branch_nm"));
			
    		//상품 등록여부 조회
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//상품 등록여부 조회 후 없으면 저장
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//있으면 과세여부, 상품종류 수정
    			sellMapper.setProductInfoForList(paramMap);
    		}
    		sInputPrice = paramMap.get("prod_price").toString();
    		
    		//단가 처리
            priceInfo = sellMapper.getProductPriceInfo(paramMap);
            if(null != priceInfo && null != priceInfo.get("prod_price")){
            	sGetPrice = priceInfo.get("prod_price").toString();
            	
            	//단가가 있는데 금액이 다르다면  종료일을 업데이트하고 새로운 단가를 생성
            	if(!sInputPrice.equals(sGetPrice)) {
            		paramMap.put("next_prod_seq", priceInfo.get("next_prod_seq"));
            		paramMap.put("cust_no", priceInfo.get("cust_no"));
            		paramMap.put("prod_no", priceInfo.get("prod_no"));
            		paramMap.put("prod_seq", priceInfo.get("prod_seq"));
	            	sellMapper.setProductPriceEndDt(paramMap);	//종료일 업뎃
	            	
	            	sellMapper.setProductPrice(paramMap);		//새로운 단가 생성
            	} 
            } else {
            	//업로드 시점에는 단가가 없을 가능성이 높기 때문에 if 처리
            	if(!"".equals(sInputPrice)) {
            		//없다면 새로 생성
            		paramMap.put("next_prod_seq", "1");
            		sellMapper.setProductPrice(paramMap);
            	}
            }
           
		}
		
		//목록으로 한꺼번에 판매내역 저장
		reqMap.put("sellList", sellist);	        
	    sellMapper.setSellForExcelUpload(reqMap);
	    
	    rtnMap.put("returnYn", "Y");
    	return rtnMap;
    }
}
