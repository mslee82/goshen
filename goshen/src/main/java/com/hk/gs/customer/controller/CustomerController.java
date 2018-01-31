package com.hk.gs.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.customer.service.CustomerService;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	@Resource(name = "customerService")
	private CustomerService customerService;
	
	/**
	 * 고객 입력 폼
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/customer/customerRegPage.do")
	public ModelAndView customerRegPage(@RequestParam Map<String, Object> pMap) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("reg_mode", "I");
		mav.setViewName("customerRegPage");
		return mav;
	}
	
	/**
	 * 고객 수정 폼
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/customer/customerModPage.do")
	public ModelAndView productModPage(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("sell_dt", pMap.get("sell_dt"));
		mav.addObject("cust_no", pMap.get("cust_no"));
		mav.addObject("sell_seq", pMap.get("sell_seq"));
		mav.addObject("reg_mode", "U");
		mav.setViewName("customerRegPage");
		return mav;
	}
	
	/**
	 * 상품 상세 정보 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/customer/getCustomerInfo.do")
	public ModelAndView getCustomerInfo(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = customerService.getCustomerInfo(pMap);
		mav.addObject("customerInfo", rtnMap); 
		return mav;
	}
	
	/**
	 * 상품 입력폼을 이용한 수정 및 등록
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/customer/setCustomer.do")
	public ModelAndView setCustomer(@RequestParam Map<String, Object> pMap)  throws Exception{		
		ModelAndView mav = new ModelAndView();
		customerService.setCustomerForForm(pMap);   

        mav.setViewName("customerList");
        return mav;
    }
	
	/**
	 * 고객 정보 화면을 return
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/customer/customerListPage.do")
	public String customerListPage(@RequestParam Map<String, Object> pMap) {
		return "customerList";
	}
	
	/**
	 * 상품 내역 조회
	 * @since 2017.12.31
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/customer/customerList.do")
	public ModelAndView productList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> customerList = customerService.getCustomerList(pMap);
		mav.addObject("list", customerList); 
		return mav;
	}
}
