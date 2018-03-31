package com.hk.gs.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.product.service.ProductPriceService;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class ProductPriceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceController.class);
	@Resource(name = "productPriceService")
	private ProductPriceService productPriceService;
	
	/**
	 * �ܰ�ǥ ��� ȭ��
	 * @since 2017.12.06
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productPriceListPage.do")
	public String productPriceListPage(Model model, @RequestParam Map<String, Object> pMap) {
		return "productPriceList";
	}

	/**
	 * �ܰ�ǥ ���
	 * @since 2017.12.06
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productPriceList.do")
	public ModelAndView productPriceList(Model model, @RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> previewList = productPriceService.productPriceList(pMap);		
		mav.addObject("list", previewList);
        return mav;
	}

	/**
	 * �ܰ�ǥ ����
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/setDelProductPrice.do")
	public ModelAndView setDelProductPrice(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productPriceService.setDelProductPrice(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * �ܰ�ǥ ����
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/setUpdProductPriceInfo.do")
	public ModelAndView setUpdProductPriceInfo(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productPriceService.setUpdProductPriceInfo(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * ���� �ܰ�ǥ ��� ȭ��
	 * @since 2018.03.04
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/comProductPriceListPage.do")
	public String comProductPriceListPage(Model model, @RequestParam Map<String, Object> pMap) {
		return "comProductPriceList";
	}
	

	/**
	 * ���� �ܰ�ǥ ���
	 * @since 2018.03.04
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/comProductPriceList.do")
	public ModelAndView comProductPriceList(Model model, @RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> previewList = productPriceService.comProductPriceList(pMap);		
		mav.addObject("list", previewList);
        return mav;
	}
	
	/**
	 * ���� �ܰ�ǥ �߰�
	 * @since 2018.03.05
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/setComProductPrice.do")
	public ModelAndView setComProductPrice(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productPriceService.setComProductPrice(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
