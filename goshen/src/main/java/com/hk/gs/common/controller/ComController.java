package com.hk.gs.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.common.service.ComService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ComController {
	
	private static final Logger logger = LoggerFactory.getLogger(ComController.class);

	@Resource(name = "comService")
	private ComService comService;
		
	/**
	 * 고객명 목록 조회
	 * @since 2017.12.12
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/com/custNmList.do")
	public ModelAndView getCustNmList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> custNmList = comService.getCustNmList(pMap);
		mav.addObject("custNmList", custNmList);
		return mav;
	}
	
	/**
	 * 상품 목록 조회
	 * @since 2017.12.12
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/com/prodNmList.do")
	public ModelAndView getProdNmList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> prodNmList = comService.getProdNmList(pMap);
		mav.addObject("prodNmList", prodNmList);
		return mav;
	}
	
	/**
	 * 단위 목록 조회
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/com/unitNmList.do")
	public ModelAndView getUnitNmList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> unitNmList = comService.getUnitNmList(pMap);
		mav.addObject("unitNmList", unitNmList);
		return mav;
	}
	
	/**
	 * 차량 목록 조회
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/com/truckNmList.do")
	public ModelAndView getTruckNmList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> truckNmList = comService.getTruckNmList(pMap);
		mav.addObject("truckNmList", truckNmList);
		return mav;
	}
}
