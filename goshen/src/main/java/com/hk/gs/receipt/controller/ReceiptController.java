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
	 * 영수증 목록 화면 return
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/receipt/viewReceipt.do")
	public String viewPage(@RequestParam Map<String, Object> pMap) throws Exception {
		return "receiptList";
	}
	
	/**
	 * 영수증 미리보기
	 * @since 2017.11.22
	 * @author 이명선
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
	 * 영수증 미리보기에서 저장
	 * @since 2018.01.24
	 * @author 이명선
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
	 * 영수증 엑셀로 다운로드
	 * @since 2017.12.09
	 * @author 이명선
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/receipt/downloadReceipt.do", method = RequestMethod.POST)
	public void downloadReceipt(@RequestParam Map<String, Object> pMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//고객정보 본지점 여부와 면세분리 발행여부 조회
		Map<String, Object> custInfo = receiptService.getCustInfo(pMap);
		
		String strMainStoreYn = (String)custInfo.get("mainstore_yn");	//본지점 구분값. 없을 수 있음.
		String strReceiptLv = (String)custInfo.get("receipt_lv");		//영수증 발행 레벨
		String strToday = (String)custInfo.get("today");				//엑셀 파일명을 만들기 위함
		String strCustNm = (String)custInfo.get("cust_nm");				//고객명 엑셀 파일명을 만들기 위함		
		String strFileNm = strCustNm + strToday + ".xls";				//파일명
		
		List<HashMap<String, Object>> sheetList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> receiptList = null; 	
		Map<String, Object> totalPrice = null;
		Map<String, Object> collMoney = null;
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> tmpList = null;		
		HashMap<String, Object> receiptMap = null;
		
		pMap.put("sMainStoreYn", strMainStoreYn);
		
		//면세 분리발행시 면세나 과세가 판매 내역이 없을땐 아예 시트를 생성하지 않도록 조회할때 조건으로 넣어준다.
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
		
		//엑셀 multi sheet 처리를 위한 조회
		if("3".equals(strReceiptLv)) {
			tmpList = receiptService.getSellDtListLv3(pMap);
		} else {
			tmpList = receiptService.getSellDtList(pMap);
		}
		
		//엑셀 multi sheet 처리를 위한 작업
		int iTmpList = tmpList.size();
		int iCnt = 0;
		for(int idx=0; idx < iTmpList; idx++) {
			receiptMap = new HashMap<String, Object>();
			
			tmpMap.put("cust_no"	, tmpList.get(idx).get("cust_no"));
			tmpMap.put("fromSellDt"	, tmpList.get(idx).get("dt_list"));
			tmpMap.put("toSellDt"	, tmpList.get(idx).get("dt_list"));
			tmpMap.put("sellType"	, pMap.get("sellType"));
			
			//면세 영수증 분리 발행 업체는 레벨을 지정해서 나눠서 조회한다.
			if("2".equals(strReceiptLv)) {
				tmpMap.put("sReceiptLv"	, tmpList.get(idx).get("receipt_lv"));
			} else {
				tmpMap.put("sReceiptLv"	, "");
			}
			
			//기간별 영수증 조회 엑셀 양식에 맞춤
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
			
			//영수증 하단의 합계란에 $[SUM(F14)] 식으로 처리하면 =N/A 오류가 나는 경우가 있어 부득이 하게 여기에서 계산으로 처리함
			for(int idx2=0; idx2 < receiptList.size(); idx2++) {
				dSumTaxFree += receiptList.get(idx2).get("tax_free") != null ? Double.parseDouble(receiptList.get(idx2).get("tax_free").toString()) : 0;
				dSumSupply += receiptList.get(idx2).get("supply") != null ? Double.parseDouble(receiptList.get(idx2).get("supply").toString()) : 0;
				dSumTax += receiptList.get(idx2).get("tax") != null ? Double.parseDouble(receiptList.get(idx2).get("tax").toString()) : 0;
			}
			dSumSubTotal = dSumSupply + dSumTax;
			dSumTotal = dSumTaxFree + dSumSubTotal;
			
			receiptMap.put("sumtaxfree", dSumTaxFree > 0 ? dSumTaxFree : "");		//면세
			receiptMap.put("sumsupply", dSumSupply > 0 ? dSumSupply : "");			//공급가
			receiptMap.put("sumtax", dSumTax > 0 ? dSumTax : "");					//부가세
			receiptMap.put("sumsubtotal", dSumSubTotal > 0 ? dSumSubTotal : "");	//과세계
			receiptMap.put("sumtotal", dSumTotal > 0 ? dSumTotal : "");				//합계
			
			//당일 합계
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
			
			//영수증레벨 3이면 등록번호, 상호 두줄로
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
		
		//받은 데이터를 맵에 담는다.
        Map<String, Object> beans = new HashMap<String, Object>();

        //엑셀 시트 목록
        beans.put("sheetList", sheetList);
        
        //영수증 상단의 발행처 정보
        beans.put("custInfo", custInfo);
        
        // 엑셀 다운로드 메소드가 담겨 있는 객체
        MakeExcel me = new MakeExcel();
        
        me.multiSheetReceiptDownload(request, response, beans, strFileNm, "receiptTemplet.xls", "xls");
        
	}
	
	/**
	 * 영수증 다운로드전 미입고 내역 조회후 메세지 처리
	 * @since 2018.02.19
	 * @author 이명선
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
