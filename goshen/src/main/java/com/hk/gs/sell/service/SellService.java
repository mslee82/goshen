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
	 * ���ּ� ���ε� �̸�����
	 * @since 2018.01.21
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<Map<String, Object>> sellUploadExePrev(MultipartFile excelFile) throws Exception {
		if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("���������� ���� �� �ּ���.");
        }
	    
	    File destFile = new File("D:\\"+excelFile.getOriginalFilename());
	    try {
	    	excelFile.transferTo(destFile);
	    } catch(IOException e) {
	    	throw new RuntimeException(e.getMessage(),e);
	    }
	        
        //Service �ܿ��� ������ �ڵ� 
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        
        //A:����, B:����, C:����, D:��ǰ��, E:����, F:����, G:����, H:�۾�ǥ���ı���        
        excelReadOption.setOutputColumns("A","B","C","D","E","F","G","H");
        excelReadOption.setStartRow(1);        
	        
        List<Map<String, Object>> excelContent = ExcelRead.read(excelReadOption);
        List<Map<String, Object>> returnContent = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> article: excelContent){
            returnContent.add(sellMapper.getSellUploadExePrev(article));
        }
    	return returnContent;
    }
	
	/**
	 * ���ּ� ���ε��� sell table��  insert
	 * @since 2017.10.22
	 * @author �̸�
	 * @throws Exception 
	 */
	/*public int setSellForExcelUpload(MultipartFile excelFile) throws Exception {
		if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("���������� ���� �� �ּ���.");
        }
	        
	    File destFile = new File("D:\\"+excelFile.getOriginalFilename());
	    try {
	    	excelFile.transferTo(destFile);
	    } catch(IOException e) {
	    	throw new RuntimeException(e.getMessage(),e);
	    }
	        
        //Service �ܿ��� ������ �ڵ� 
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
    }*/
	
	/**
	 * �Ǹ� �Է����� �̿��� ���� �� ���
	 * @since 2017.10.22
	 * @author �̸�
	 * @throws Exception 
	 */
	public int setSellForForm(Map<String, Object> map) throws Exception {    		
		int iRst = 0;
		//merge ���Ұ�. sell_seq ������ duplicate �߻� ����.
		//���� Ȥ�� ���
		if("I".equals(map.get("mode"))){
			iRst = sellMapper.setInsSellForForm(map);
		} else {
			iRst = sellMapper.setUpdSellForForm(map);
		}
    	return iRst;
    }
	
	/**
	 * �Ǹ� ���� ��ȸ
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellList(Map<String, Object> map) throws Exception {		
    	return sellMapper.getSellList(map);		
    }
	
	/**
	 * �Ǹ� ���� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> getSellInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = sellMapper.getSellInfo(map);
    	return rtnMap;
    }
	
	/**
	 * �Ǹŵ��
	 * @since 2018.01.21
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> setSellList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> tempMap;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int iProdCnt = 0;
		List<Map<String, Object>> sellist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			
			//��ǰ�� " ��ȣ replace
			tempMap.put("prod_nm", tempMap.get("prod_nm").toString().replaceAll("&quot;", "\""));
			sellist.add(tempMap);
			
			paramMap.put("prod_nm", tempMap.get("prod_nm"));
			paramMap.put("prod_typ", tempMap.get("prod_typ"));
			paramMap.put("prod_price", tempMap.get("prod_price"));
			paramMap.put("cust_nm", tempMap.get("cust_nm"));
			paramMap.put("tax_yn", tempMap.get("tax_yn"));
			paramMap.put("sell_dt", tempMap.get("sell_dt"));
			paramMap.put("branch_nm", tempMap.get("branch_nm"));
			
    		//��ǰ ��Ͽ��� ��ȸ
    		iProdCnt = sellMapper.getProductInfo(paramMap);
    		
    		if(iProdCnt == 0) {
    			//��ǰ ��Ͽ��� ��ȸ �� ������ ����
    			sellMapper.setProductForList(paramMap);
    		} else{
    			//������ ��������, ��ǰ���� ����
    			sellMapper.setProductInfoForList(paramMap);
    		}
		}
		
		//������� �Ѳ����� �Ǹų��� ����
		reqMap.put("sellList", sellist);	        
	    sellMapper.setSellForExcelUpload(reqMap);
	    
	    rtnMap.put("returnYn", "Y");
    	return rtnMap;
    }
	
	/**
	 * �Ǹ� ���� ��ȸ���� ����
	 * @since 2018.03.11
	 * @author �̸�
	 * @throws Exception 
	 */
	public Map<String, Object> delSellList(Map<String, Object> map) throws Exception {    		
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("sell_dt");
			map.remove("cust_no");
			map.remove("sell_seq");
			
    		map.put("sell_dt", tempMap.get("sell_dt"));
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("sell_seq", tempMap.get("sell_seq"));
    		iRst = sellMapper.delSellList(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("sell_seq"));
    		}
		}
		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
}
