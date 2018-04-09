package com.hk.gs.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
 
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.XlsArea;
import org.jxls.command.Command;
import org.jxls.command.EachCommand;
import org.jxls.command.IfCommand;
import org.jxls.common.AreaRef;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
 
// MakeExcel이라는 클래스를 만들고 그 안에 download라는 메소드를 생성한다.
public class MakeExcel {
    public void download(HttpServletRequest request, HttpServletResponse response,
                    Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException {
 
        // 받아오는 매개변수 bean는 디비에서 뽑아온 데이터
        // fileName 은 다운로드 받을때 지정되는 파일명
        // templateFile 는 템플릿 엑셀 파일명이다.
        
        // tempPath는 템플릿 엑셀파일이 들어가는 경로를 넣어 준다.
        //String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/excel");
        
        String tempPath = "C:\\work";
        // 별도로 다운로드 만들기 귀찮으까 이런식으로 만들어서 바로 엑셀 생성후 다운 받게 
        try {
 
            InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile));
            XLSTransformer xls = new XLSTransformer();
                        
            Workbook workbook = xls.transformXLS(is, bean);            
            
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            
            OutputStream os = response.getOutputStream();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    //면세분리, 지점보유, 월 누적
    public void multiSheetReceiptDownload(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 
    	
        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //템플릿 경로 지정
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //저장되는 파일명
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            //전체 템플릿 영역 지정  Template!A1:K15 -> 시트명!시작점:종료점
            XlsArea xlsArea = new XlsArea("Template!A1:M20", transformer);
            
            //
            XlsArea sheetArea = new XlsArea("Template!A2:M19", transformer);
            
            //multi sheet command 단순히 List를 표현하기 위해서라면 이것을 사용할 필요가 없음. 고센의 경우 월,일별로 시트를 모으므로 이것은 일자를 기준으로 반복함.
            //1,2 항목이 동적으로 면세분리면 두 개의 리스트가, 지점보유라면 두 배의 리스트가, 월누적이라면 앞부분을 포함한 누적일자 만큼이 그때는 이부분이 selldt쯤? 
            EachCommand sheetEachCommand = new EachCommand("sheetList", "sheetLists", sheetArea, new SimpleCellRefGenerator());
            
            //영수증 목록의 영역을 지정함. 
            XlsArea excelArea = new XlsArea("Template!A14:L14", transformer);

            //영수증 목록 데이터셋을 지정
            Command receiptEachCommand = new EachCommand("receiptData", "sheetList.receiptData", excelArea);
            sheetArea.addCommand(new AreaRef("Template!A14:L14"), receiptEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:L19"), sheetEachCommand); //multi sheet command를 영역에 추가함.
            
            //데이터셋은 여기에 putVar로 지정해서 사용
            Context context = new Context();
            context.putVar("sheetLists", bean.get("sheetList"));	//시트명을 위한 작업. 일별, 지점별로 구분, 면세과세별
            //context.putVar("dataList", bean.get("dataList"));		//실제 영수증 내역
            
            //조회기간의 총 합계 시트
            
            //영수증하단의 일 합계 누적
            
            //저장되는 시트명과 시작점
            xlsArea.applyAt(new CellRef("Template!A1"), context);
            xlsArea.processFormulas();    
            
            //템플릿 시트 삭제
            transformer.deleteSheet("Template");
            transformer.deleteSheet("Total");
            
            //실제 파일 내보내기
            transformer.write();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}