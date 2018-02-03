package com.hk.gs.sell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.sell.mapper.SellReturnMapper;
import com.hk.gs.util.CommUtil;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("sellReturnService")
public class SellReturnService {
	
	private static final Logger logger = LoggerFactory.getLogger(SellReturnService.class);
	
	@Resource(name="sellReturnMapper")
    private SellReturnMapper sellReturnMapper;
	
	/**
	 * 판매 반품 등록
	 * @since 2017.12.13
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setSellReturn(Map<String, Object> map) throws Exception {
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
			map.remove("return_quan");
			
    		map.put("sell_dt", tempMap.get("sell_dt"));
    		map.put("cust_no", tempMap.get("cust_no"));
    		map.put("sell_seq", tempMap.get("sell_seq"));
    		map.put("return_quan", tempMap.get("return_quan"));
    		iRst =  sellReturnMapper.setSellReturn(map);
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
	
	/**
	 * 판매 반품 조회
	 * @since 2017.12.16
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellReturnList(Map<String, Object> map) throws Exception {
    	return sellReturnMapper.getSellReturnList(map);		
    }
	
	/**
	 * 반품 삭제(취소)
	 * @since 2017.12.16
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setDelSellReturn(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("return_dt");
			map.remove("return_seq");
    		map.put("return_dt", tempMap.get("return_dt"));
    		map.put("return_seq", tempMap.get("return_seq"));
    		
    		iRst =  sellReturnMapper.setDelSellReturn(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("return_seq"));
    		}
		}		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
	/**
	 * 판매 반품 수량 수정
	 * @since 2018.02.01
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> setUpdSellReturn(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> jsonData = CommUtil.json2List(String.valueOf(map.get("jsonData")));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int iCnt = 0;
		int iRst = 0;
		StringBuffer strBuff = new StringBuffer();
		Map<String, Object> tempMap;		
		for (int i = 0; i < jsonData.size(); i++) {
			tempMap = jsonData.get(i);
			map.remove("return_dt");
			map.remove("return_seq");
			map.remove("return_quan");
			
			map.put("return_dt", tempMap.get("return_dt"));
    		map.put("return_seq", tempMap.get("return_seq"));
    		map.put("return_quan", tempMap.get("return_quan"));
    		iRst =  sellReturnMapper.setUpdSellReturn(map);
    		if(iRst >= 1){
    			iCnt++;
    		} else{
    			if(strBuff.length() > 0){
					strBuff.append(",");
				}
				strBuff.append(jsonData.get(i).get("return_seq"));
    		}
		}
		
		rtnMap.put("returnCnt", iCnt);
		rtnMap.put("returnMsg", strBuff.toString());
    	return rtnMap;
    }
	
}
