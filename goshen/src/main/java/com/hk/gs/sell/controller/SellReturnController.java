package com.hk.gs.sell.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.sell.service.SellReturnService;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class SellReturnController {
	
	private static final Logger logger = LoggerFactory.getLogger(SellReturnController.class);
	@Resource(name = "sellReturnService")
	private SellReturnService sellReturnService;

	
	/**
	 * �Ǹ� ��ǰ
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sellReturn/setSellReturn.do")
	public ModelAndView setSellReturn(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellReturnService.setSellReturn(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * ��ǰ ����  ȭ���� return
	 * @since 2017.12.16
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sellReturn/sellReturnListPage.do")
	public String sellReturnListPage(@RequestParam Map<String, Object> pMap) {
		return "sellReturnList";
	}
	
	/**
	 * ��ǰ ���� ��ȸ
	 * @since 2017.12.16
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sellReturn/sellReturnList.do")
	public ModelAndView sellReturnList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> sellReturnList = sellReturnService.getSellReturnList(pMap);
		mav.addObject("list", sellReturnList); 
		return mav;
	}
	
	/**
	 * ��ǰ����(���)
	 * @since 2017.12.16
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sellReturn/setDelSellReturn.do")
	public ModelAndView setDelSellReturn(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellReturnService.setDelSellReturn(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}

	/**
	 * �Ǹ� ��ǰ ���� ����
	 * @since 2018.02.01
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sellReturn/setModSellReturn.do")
	public ModelAndView setModSellReturn(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellReturnService.setUpdSellReturn(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
