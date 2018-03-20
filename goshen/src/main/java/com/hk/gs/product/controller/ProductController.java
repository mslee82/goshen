package com.hk.gs.product.controller;

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

import com.hk.gs.product.service.ProductService;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Resource(name = "productService")
	private ProductService productService;
	
	/**
	 * ��ǰ �Է� ��
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productRegPage.do")
	public ModelAndView productRegPage(@RequestParam Map<String, Object> pMap) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("reg_mode", "I");
		mav.setViewName("productRegPage");
		return mav;
	}
	
	/**
	 * ��ǰ ���� ��
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productModPage.do")
	public ModelAndView productModPage(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("sell_dt", pMap.get("sell_dt"));
		mav.addObject("cust_no", pMap.get("cust_no"));
		mav.addObject("sell_seq", pMap.get("sell_seq"));
		mav.addObject("reg_mode", "U");
		mav.setViewName("productRegPage");
		return mav;
	}
	
	/**
	 * ��ǰ �� ���� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/getProductInfo.do")
	public ModelAndView getProductInfo(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productService.getProductInfo(pMap);
		mav.addObject("productInfo", rtnMap); 
		return mav;
	}
	
	/**
	 * ��ǰ �Է����� �̿��� ���� �� ���
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/product/setProduct.do")
	public ModelAndView setProduct(@RequestParam Map<String, Object> pMap)  throws Exception{		
		ModelAndView mav = new ModelAndView();
		productService.setProductForForm(pMap);   

        mav.setViewName("productList");
        return mav;
    }
	
	/**
	 * ��ǰ ���  ȭ���� return
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productListPage.do")
	public String productListPage(@RequestParam Map<String, Object> pMap) {
		return "productList";
	}
	
	/**
	 * ��ǰ ��� ��ȸ
	 * @since 2017.12.31
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productList.do")
	public ModelAndView productList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> productList = productService.getProductList(pMap);
		mav.addObject("list", productList); 
		return mav;
	}
	
	/**
	 * ��ǰ ��Ͽ��� ����
	 * @since 2018.03.20
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/setProductList.do")
	public ModelAndView setProductList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productService.setProductList(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
