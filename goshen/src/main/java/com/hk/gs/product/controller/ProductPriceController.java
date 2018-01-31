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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
	 * 단가표 업로드 화면
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productPriceUpload.do")
	public String productUploadPage(Model model, @RequestParam Map<String, Object> pMap) {
		return "productPriceUpload";
	}

	/**
	 * 단가표 업로드
	 * @since 2017.10.22
	 * @author 이명선
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/product/productUploadExe.do", method = RequestMethod.POST)
	public ModelAndView productUploadExe(MultipartHttpServletRequest request)  throws Exception{		
        MultipartFile excelFile = request.getFile("excel");
        System.out.println("엑셀 파일 업로드 컨트롤러");
        
        /**
         * 단가표 업로드
         * 없을때는 시작일자 to day 종료일자 9999.12.31로 insert
         * 있을때는 기존 데이터의 종료일자를 입력일자-1 update 후 없을때 처럼 insert
         */
        productPriceService.setProductPriceForExcelUpload(excelFile);   
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("msg", "단가표 업로드 완료");
        return mav;
    }
	
	/**
	 * 단가표 목록 화면
	 * @since 2017.12.06
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/productPriceListPage.do")
	public String productPriceListPage(Model model, @RequestParam Map<String, Object> pMap) {
		return "productPriceList";
	}

	/**
	 * 단가표 목록
	 * @since 2017.12.06
	 * @author 이명선
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
	 * 단가표 삭제
	 * @since 2017.12.17
	 * @author 이명선
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
	 * 단가표 수정
	 * @since 2017.12.17
	 * @author 이명선
	 * @throws Exception 
	 */
	@RequestMapping(value = "/product/setUpdProductPriceInfo.do")
	public ModelAndView setUpdProductPriceInfo(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = productPriceService.setUpdProductPriceInfo(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
