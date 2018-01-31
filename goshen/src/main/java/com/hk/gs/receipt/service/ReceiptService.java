package com.hk.gs.receipt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hk.gs.receipt.mapper.ReceiptMapper;

/**
 * Handles requests for the application home page.
 */
@Repository
@Service("receiptService")
public class ReceiptService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
	
	@Resource(name = "receiptMapper")
    private ReceiptMapper receiptMapper;
	
	/**
	 * 영수증 미리보기
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> previewReceipt(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getReceiptList(map);		
    }
	
	/**
	 * 영수증 기본정보
	 * @since 2017.12.10
	 * @author 이명선
	 * @throws Exception 
	 */
	public Map<String, Object> getCustInfo(Map<String, Object> map) throws Exception {		
    	return receiptMapper.getCustInfo(map);		
    }
	
	/**
	 * 영수증 엑셀 시트 목록
	 * @since 2017.12.24
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtList(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtList(map);		
    }
	
	/**
	 * 영수증 엑셀 시트 목록
	 * @since 2017.12.24
	 * @author 이명선
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getSellDtListForBranch(Map<String, Object> map) throws Exception {
		
    	return receiptMapper.getSellDtListForBranch(map);		
    }
}
