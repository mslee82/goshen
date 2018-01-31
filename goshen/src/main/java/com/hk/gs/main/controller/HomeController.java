package com.hk.gs.main.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.main.service.HomeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Resource(name = "homeService")
	private HomeService homeService;
		
	/**
	 * 메인
	 * @since 2017.10.12
	 * @author 이명선
	 */
	@RequestMapping(value = "/main/home.do", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @RequestParam Map<String, Object> pMap) throws Exception {
		//Locale locale = ;
		//Model model;
		logger.info("Welcome home! The client locale is {}.", locale);
		ModelAndView mav = new ModelAndView("home");
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);		
		
		//List<HashMap<String, Object>> res = homeService.test(pMap);
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("aa", "한글");
		return "home";
	}

	/**
	 * 거래처 매출 현황 TOP5
	 * @since 2017.12.27
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/main/customerSalesTop5.do")
	public ModelAndView customerSalesTop5(Model model, @RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> customerSalesList = homeService.customerSalesTop5(pMap);		
		mav.addObject("chart", customerSalesList);
        return mav;
	}

	/**
	 * 최대 판매액 TOP5
	 * @since 2017.12.27
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/main/bestSellingProduct.do")
	public ModelAndView bestSellingProduct(Model model, @RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> previewList = homeService.customerSalesTop5(pMap);		
		mav.addObject("list", previewList);
        return mav;
	}
	
}
