package com.hk.gs.sell.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.sell.service.DeliveryInfoService;
import com.hk.gs.util.MakeExcel;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class DeliveryInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryInfoController.class);
	@Resource(name = "deliveryInfoService")
	private DeliveryInfoService deliveryInfoService;

	/**
	 * 배송정보 조회 화면을 return
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/deliveryInfoListPage.do")
	public String deliveryInfoListPage(@RequestParam Map<String, Object> pMap) {
		return "deliveryInfoList";
	}
	
	/**
	 * 배송기사용 배송정보 조회
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/deliveryInfoList.do")
	public ModelAndView getDeliveryListForDrivers(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> deliveryList = deliveryInfoService.getDeliveryListForDrivers(pMap);
		mav.addObject("list", deliveryList); 
		return mav;
	}	
	
	/**
	 * 영수증 엑셀로 다운로드
	 * @since 2017.12.18
	 * @author 이명선
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/sell/downloadDeliveryList.do", method = RequestMethod.POST)
	public void downloadDeliveryList(@RequestParam Map<String, Object> pMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//영수증 내역 조회
		List<HashMap<String, Object>> deliveryList = deliveryInfoService.getDeliveryListForDrivers(pMap);
		Map<String, Object> today = deliveryInfoService.getToday(pMap);
		// 받은 데이터를 맵에 담는다.
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("dataList", deliveryList);
        beans.put("todayInfo", today);
        //beans.put("trcukInfo", trcukInfo);
        
        // 엑셀 다운로드 메소드가 담겨 있는 객체
        MakeExcel me = new MakeExcel();

        me.download(request, response, beans, today.get("file_nm").toString()+".xls", "deliveryTemplet.xls", "xls");
	}
}
