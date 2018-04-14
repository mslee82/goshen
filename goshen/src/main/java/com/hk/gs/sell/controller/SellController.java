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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hk.gs.sell.service.SellService;

/**
 * Handles requests for the application home page.
 */
@Repository
@Controller
public class SellController {
	
	private static final Logger logger = LoggerFactory.getLogger(SellController.class);
	@Resource(name = "sellService")
	private SellService sellService;
	
	/**
	 * ���ּ� ���ε� �ϴ� ȭ���� return
	 * @since 2017.10.22
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellUpload.do")
	public String sellUploadPage(@RequestParam Map<String, Object> pMap) {;
		return "sellUpload";
	}
	
	/**
	 * ���ּ� ���ε� �̸�����
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellUploadExePrev.do")
	public ModelAndView setSellForExcelUploadPrev(MultipartHttpServletRequest request)  throws Exception{	
		MultipartFile excelFile = request.getFile("excel");
		ModelAndView mav = new ModelAndView("jsonView");
		List<Map<String, Object>> sellListPrev = sellService.sellUploadExePrev(excelFile);
		mav.addObject("list", sellListPrev); 
		return mav;
	}
	
	/**
	 * �Ǹ� �Է����� �̿��� ���� �� ���
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/sell/setSell.do")
	public ModelAndView setSell(@RequestParam Map<String, Object> pMap)  throws Exception{		
		ModelAndView mav = new ModelAndView();
        sellService.setSellForForm(pMap);   

        mav.setViewName("sellList");
        return mav;
    }
	
	/**
	 * �Ǹ� ����  ȭ���� return
	 * @since 2017.12.11
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellListPage.do")
	public String sellListPage(@RequestParam Map<String, Object> pMap) {
		return "sellList";
	}
	
	/**
	 * �Ǹ� ���� ��ȸ
	 * @since 2017.12.12
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellList.do")
	public ModelAndView sellList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> sellList = sellService.getSellList(pMap);
		mav.addObject("list", sellList); 
		return mav;
	}
	
	/**
	 * �Ǹ� �Է� ��
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellRegPage.do")
	public ModelAndView sellRegPage(@RequestParam Map<String, Object> pMap) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("reg_mode", "I");
		mav.setViewName("sellRegMod");
		return mav;
	}
	
	/**
	 * �Ǹ� ���� ��
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/sellModPage.do")
	public ModelAndView sellModPage(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("sell_dt", pMap.get("sell_dt"));
		mav.addObject("cust_no", pMap.get("cust_no"));
		mav.addObject("sell_seq", pMap.get("sell_seq"));
		mav.addObject("reg_mode", "U");
		mav.setViewName("sellRegMod");
		return mav;
	}
	
	/**
	 * �Ǹ� �� ���� ��ȸ
	 * @since 2017.12.17
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/getSellInfo.do")
	public ModelAndView getSellInfo(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellService.getSellInfo(pMap);
		mav.addObject("sellInfo", rtnMap); 
		return mav;
	}
	
	/**
	 * �ǸŹ̸����⿡�� ����
	 * @since 2018.01.24
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/setSellList.do")
	public ModelAndView setSellList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellService.setSellList(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * �Ǹ� ���� ��ȸ���� ����
	 * @since 2018.03.11
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/delSellList.do")
	public ModelAndView delSellList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellService.delSellList(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	
	/**
	 * ���ݰ��� ȭ���� return
	 * @since 2018.04.13
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/collMoneyListPage.do")
	public String collMoneyListPage(@RequestParam Map<String, Object> pMap) {
		return "collMoneyList";
	}
	
	/**
	 * ���� �̼��� ��� ��ȸ
	 * @since 2018.04.13
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/collMoneyList.do")
	public ModelAndView getCollMoneyList(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<HashMap<String, Object>> collMoneyList = sellService.getCollMoneyList(pMap);
		mav.addObject("list", collMoneyList); 
		return mav;
	}
	
	/**
	 * ���� �̼��� ���� �� ����
	 * @since 2018.04.13
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/setCollMoney.do")
	public ModelAndView setCollMoney(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellService.setCollMoney(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
	
	/**
	 * ���� �̼��� ����
	 * @since 2018.04.13
	 * @author �̸�
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sell/delCollMoney.do")
	public ModelAndView delCollMoney(@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> rtnMap = sellService.delCollMoney(pMap);
		
		mav.addObject("result", rtnMap); 
		return mav;
	}
}
