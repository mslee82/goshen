package com.hk.gs.util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxls.command.CellRefGenerator;
import org.jxls.common.CellRef;
import org.jxls.common.Context;

/**
 * @author Leonid Vysochyn
 */
public class SimpleCellRefGenerator implements CellRefGenerator {
    public CellRef generateCellRef(int index, Context context) {
    	List<HashMap<String, Object>> sheetLists = (List<HashMap<String, Object>>) context.getVar("sheetLists");
    	Map<String, Object> sheetList = sheetLists.get(index);
    	StringBuffer strBuff = new StringBuffer();
    	if(null != sheetList.get("branch_nm") && !"".equals(sheetList.get("branch_nm"))) {
	    	strBuff.append(sheetList.get("branch_nm"));
    	}    	
    	if(null != sheetList.get("receipt_lv") && "2".equals(sheetList.get("receipt_lv").toString())) {
    		strBuff.append(sheetList.get("company_nm"));	
    	}
    	strBuff.append(sheetList.get("dt_list"));
    	
    	String sheetName = strBuff.toString();
        return new CellRef(sheetName + "!A2");
    }
}