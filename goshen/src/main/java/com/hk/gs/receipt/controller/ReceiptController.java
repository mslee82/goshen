package com.hk.gs.receipt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.receipt.service.ReceiptService;
import com.hk.gs.util.MakeExcel;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ReceiptController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

	@Resource(name = "receiptService")
	private ReceiptService receiptService;
	
	/**
	 * ������ ��� ȭ�� return
	 * @since 2017.10.22
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/receipt/viewReceipt.do")
	public String viewPage(@RequestParam Map<String, Object> pMap) throws Exception {
		return "receiptList";
	}
	
	/**
	 * ������ �̸�����
	 * @since 2017.11.22
	 * @author �̸�
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/receipt/previewReceipt.do")
	public ModelAndView privewReceipt(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> previewList = receiptService.previewReceipt(pMap);		
		mav.addObject("list", previewList);
		
        return mav;
	}
	
	/**
	 * ������ �̸����⿡�� ����
	 * @since 2018.01.24
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/receipt/setSellList.do")
	public ModelAndView setSellPrice(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = receiptService.setSellList(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * ������ ������ �ٿ�ε�
	 * @since 2017.12.09
	 * @author �̸�
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/receipt/downloadReceipt.do", method = RequestMethod.POST)
	public void downloadReceipt(@RequestParam Map<String, Object> pMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//������ ������ ���ο� �鼼�и� ���࿩�� ��ȸ
		Map<String, Object> custInfo = receiptService.getCustInfo(pMap);
		
		String strMainStoreYn = (String)custInfo.get("mainstore_yn");	//������ ���а�. ���� �� ����.
		String strReceiptLv = (String)custInfo.get("receipt_lv");		//������ ���� ����
		String strToday = (String)custInfo.get("today");				//���� ���ϸ��� ����� ����
		String strCustNm = (String)custInfo.get("cust_nm");				//���� ���� ���ϸ��� ����� ����		
		String strFileNm = strCustNm + strToday + ".xls";				//���ϸ�
		
		List<HashMap<String, Object>> sheetList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> receiptList = null; 	
		Map<String, Object> totalPrice = null;
		Map<String, Object> collMoney = null;
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> tmpList = null;		
		HashMap<String, Object> receiptMap = null;
		
		pMap.put("sMainStoreYn", strMainStoreYn);
		
		//�鼼 �и������ �鼼�� ������ �Ǹ� ������ ������ �ƿ� ��Ʈ�� �������� �ʵ��� ��ȸ�Ҷ� �������� �־��ش�.
		if("2".equals(strReceiptLv)) {
			pMap.put("sReceiptLv", strReceiptLv);
		} else {
			pMap.put("sReceiptLv", "");
		}

		double dSumTaxFree = 0;
		double dSumSupply = 0;
		double dSumTax = 0;
		double dSumSubTotal = 0;
		double dSumTotal = 0;
		
		//���� multi sheet ó���� ���� ��ȸ
		if("3".equals(strReceiptLv)) {
			tmpList = receiptService.getSellDtListLv3(pMap);
		} else {
			tmpList = receiptService.getSellDtList(pMap);
		}
		
		//���� multi sheet ó���� ���� �۾�
		int iTmpList = tmpList.size();
		int iCnt = 0;
		for(int idx=0; idx < iTmpList; idx++) {
			receiptMap = new HashMap<String, Object>();
			
			tmpMap.put("cust_no"	, tmpList.get(idx).get("cust_no"));
			tmpMap.put("fromSellDt"	, tmpList.get(idx).get("dt_list"));
			tmpMap.put("toSellDt"	, tmpList.get(idx).get("dt_list"));
			tmpMap.put("sellType"	, pMap.get("sellType"));
			
			//�鼼 ������ �и� ���� ��ü�� ������ �����ؼ� ������ ��ȸ�Ѵ�.
			if("2".equals(strReceiptLv)) {
				tmpMap.put("sReceiptLv"	, tmpList.get(idx).get("receipt_lv"));
			} else {
				tmpMap.put("sReceiptLv"	, "");
			}
			
			//�Ⱓ�� ������ ��ȸ ���� ��Ŀ� ����
			receiptList = receiptService.previewReceipt(tmpMap);
			
			if(receiptList.size() == 0) {
				continue;
			}
			//if(idx > 0) {
				totalPrice = receiptService.getSellTotalPrice(tmpMap);
				collMoney = receiptService.getCollMoneyType2Amt(tmpMap);
			/*} else {
				totalPrice = new HashMap<String, Object>();
				totalPrice.put("sell_dt", tmpMap.get("fromSellDt"));
				totalPrice.put("total_price", 0);
			}*/
			
			//������ �ϴ��� �հ���� $[SUM(F14)] ������ ó���ϸ� =N/A ������ ���� ��찡 �־� �ε��� �ϰ� ���⿡�� ������� ó����
			for(int idx2=0; idx2 < receiptList.size(); idx2++) {
				dSumTaxFree += receiptList.get(idx2).get("tax_free") != null ? Double.parseDouble(receiptList.get(idx2).get("tax_free").toString()) : 0;
				dSumSupply += receiptList.get(idx2).get("supply") != null ? Double.parseDouble(receiptList.get(idx2).get("supply").toString()) : 0;
				dSumTax += receiptList.get(idx2).get("tax") != null ? Double.parseDouble(receiptList.get(idx2).get("tax").toString()) : 0;
			}
			dSumSubTotal = dSumSupply + dSumTax;
			dSumTotal = dSumTaxFree + dSumSubTotal;
			
			receiptMap.put("sumtaxfree", dSumTaxFree > 0 ? dSumTaxFree : "");		//�鼼
			receiptMap.put("sumsupply", dSumSupply > 0 ? dSumSupply : "");			//���ް�
			receiptMap.put("sumtax", dSumTax > 0 ? dSumTax : "");					//�ΰ���
			receiptMap.put("sumsubtotal", dSumSubTotal > 0 ? dSumSubTotal : "");	//������
			receiptMap.put("sumtotal", dSumTotal > 0 ? dSumTotal : "");				//�հ�
			
			//���� �հ�
			if(receiptList != null) {
				receiptMap.put("receiptData", receiptList);
			}
			if(totalPrice != null) {
				receiptMap.put("total_price", totalPrice.get("total_price"));
			}
			if(collMoney != null) {
				receiptMap.put("coll_money", collMoney.get("amt"));
			}
			
			receiptMap.put("dt_list"	, tmpList.get(idx).get("dt_list"));
			receiptMap.put("account"	, tmpList.get(idx).get("account"));
			receiptMap.put("bank_nm"	, tmpList.get(idx).get("bank_nm"));
			
			//���������� 3�̸� ��Ϲ�ȣ, ��ȣ ���ٷ�
			if(tmpList.get(idx).get("reg_no").toString().indexOf(",") > 0) {
				receiptMap.put("company_nm", tmpList.get(idx).get("company_nm").toString().replaceAll(",", "\n"));
				receiptMap.put("reg_no"	, tmpList.get(idx).get("reg_no").toString().replaceAll(",", "\n"));
			} else{
				receiptMap.put("company_nm", tmpList.get(idx).get("company_nm"));
				receiptMap.put("reg_no"	, tmpList.get(idx).get("reg_no"));
			}
			
			receiptMap.put("phone"		, tmpList.get(idx).get("phone"));
			receiptMap.put("cust_no"	, tmpList.get(idx).get("cust_no"));
			receiptMap.put("cust_nm"	, tmpList.get(idx).get("cust_nm"));
			receiptMap.put("receipt_lv"	, strReceiptLv);
			receiptMap.put("branch_nm"	, tmpList.get(idx).get("branch_nm"));
			sheetList.add(iCnt, receiptMap);
			iCnt ++;
		}
		
		//���� �����͸� �ʿ� ��´�.
        Map<String, Object> beans = new HashMap<String, Object>();

        //���� ��Ʈ ���
        beans.put("sheetList", sheetList);
        
        //������ ����� ����ó ����
        beans.put("custInfo", custInfo);
        
        // ���� �ٿ�ε� �޼ҵ尡 ��� �ִ� ��ü
        MakeExcel me = new MakeExcel();
        
        me.multiSheetReceiptDownload(request, response, beans, strFileNm, "receiptTemplet.xls", "xls");
        
	}
	
	/**
	 * ������ �ٿ�ε��� ���԰� ���� ��ȸ�� �޼��� ó��
	 * @since 2018.02.19
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/receipt/getCheckSellType.do")
	public ModelAndView getCheckSellType(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = receiptService.getCheckSellType(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
