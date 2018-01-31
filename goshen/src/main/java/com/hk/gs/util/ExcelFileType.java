package com.hk.gs.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileType {
	
	/**
	 * �뿊���뙆�씪�쓣혻�씫�뼱�꽌혻Workbook혻媛앹껜�뿉혻由ы꽩�븳�떎.
혻혻혻혻혻*혻XLS��혻XLSX혻�솗�옣�옄瑜셋좊퉬援먰븳�떎.
혻혻혻혻혻*혻
혻혻혻혻혻*혻@param혻filePath
혻혻혻혻혻*혻@return
	 */
	public static Workbook getWorkbook(String filePath) {
		/*
		 * FileInputStream��혻�뙆�씪�쓽혻寃쎈줈�뿉혻�엳�뒗혻�뙆�씪�쓣
		 * �씫�뼱�꽌혻Byte濡쑣좉��졇�삩�떎.
혻혻혻혻혻혻혻혻혻*혻
혻혻혻혻혻혻혻혻혻*혻�뙆�씪�씠혻議댁옱�븯吏�혻�븡�뒗�떎硫댁�
혻혻혻혻혻혻혻혻혻*혻RuntimeException�씠혻諛쒖깮�맂�떎.
		 */	
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		Workbook wb = null;
		/*
		 * �뙆�씪�쓽혻�솗�옣�옄瑜셋좎껜�겕�빐�꽌혻.XLS혻�씪硫는쟄SSFWorkbook�뿉
		 * .XLSX�씪硫는쟚SSFWorkbook�뿉혻媛곴컖혻珥덇린�솕혻�븳�떎.
		 */

		if(filePath.toUpperCase().endsWith(".XLS")) {
			try {
				wb = new HSSFWorkbook(fis);
			} catch(IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else if (filePath.toUpperCase().endsWith(".XLSX")) {
			try {
				wb = new XSSFWorkbook(fis);
			} catch(IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return wb;
	}
}
