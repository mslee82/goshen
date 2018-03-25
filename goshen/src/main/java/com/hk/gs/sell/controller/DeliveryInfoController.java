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
	 * ������� ��ȸ ȭ���� return
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/deliveryInfoListPage.do")
	public String deliveryInfoListPage(@RequestParam Map<String, Object> pMap) {
		return "deliveryInfoList";
	}
	
	/**
	 * ��۱��� ������� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
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
	 * ������ ������ �ٿ�ε�
	 * @since 2017.12.18
	 * @author �̸�
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/sell/downloadDeliveryList.do", method = RequestMethod.POST)
	public void downloadDeliveryList(@RequestParam Map<String, Object> pMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//������ ���� ��ȸ
		List<HashMap<String, Object>> deliveryList = deliveryInfoService.getDeliveryListForDrivers(pMap);
		Map<String, Object> today = deliveryInfoService.getToday(pMap);
		// ���� �����͸� �ʿ� ��´�.
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("dataList", deliveryList);
        beans.put("todayInfo", today);
        //beans.put("trcukInfo", trcukInfo);
        
        // ���� �ٿ�ε� �޼ҵ尡 ��� �ִ� ��ü
        MakeExcel me = new MakeExcel();

        me.download(request, response, beans, today.get("file_nm").toString()+".xls", "deliveryTemplet.xls", "xls");
	}
}
