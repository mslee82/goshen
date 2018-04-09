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
 
// MakeExcel�̶�� Ŭ������ ����� �� �ȿ� download��� �޼ҵ带 �����Ѵ�.
public class MakeExcel {
    public void download(HttpServletRequest request, HttpServletResponse response,
                    Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException {
 
        // �޾ƿ��� �Ű����� bean�� ��񿡼� �̾ƿ� ������
        // fileName �� �ٿ�ε� ������ �����Ǵ� ���ϸ�
        // templateFile �� ���ø� ���� ���ϸ��̴�.
        
        // tempPath�� ���ø� ���������� ���� ��θ� �־� �ش�.
        //String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/excel");
        
        String tempPath = "C:\\work";
        // ������ �ٿ�ε� ����� �������� �̷������� ���� �ٷ� ���� ������ �ٿ� �ް� 
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
    
    //�鼼�и�, ��������, �� ����
    public void multiSheetReceiptDownload(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 
    	
        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //���ø� ��� ����
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            //��ü ���ø� ���� ����  Template!A1:K15 -> ��Ʈ��!������:������
            XlsArea xlsArea = new XlsArea("Template!A1:M20", transformer);
            
            //
            XlsArea sheetArea = new XlsArea("Template!A2:M19", transformer);
            
            //multi sheet command �ܼ��� List�� ǥ���ϱ� ���ؼ���� �̰��� ����� �ʿ䰡 ����. ���� ��� ��,�Ϻ��� ��Ʈ�� �����Ƿ� �̰��� ���ڸ� �������� �ݺ���.
            //1,2 �׸��� �������� �鼼�и��� �� ���� ����Ʈ��, ����������� �� ���� ����Ʈ��, �������̶�� �պκ��� ������ �������� ��ŭ�� �׶��� �̺κ��� selldt��? 
            EachCommand sheetEachCommand = new EachCommand("sheetList", "sheetLists", sheetArea, new SimpleCellRefGenerator());
            
            //������ ����� ������ ������. 
            XlsArea excelArea = new XlsArea("Template!A14:L14", transformer);

            //������ ��� �����ͼ��� ����
            Command receiptEachCommand = new EachCommand("receiptData", "sheetList.receiptData", excelArea);
            sheetArea.addCommand(new AreaRef("Template!A14:L14"), receiptEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:L19"), sheetEachCommand); //multi sheet command�� ������ �߰���.
            
            //�����ͼ��� ���⿡ putVar�� �����ؼ� ���
            Context context = new Context();
            context.putVar("sheetLists", bean.get("sheetList"));	//��Ʈ���� ���� �۾�. �Ϻ�, �������� ����, �鼼������
            //context.putVar("dataList", bean.get("dataList"));		//���� ������ ����
            
            //��ȸ�Ⱓ�� �� �հ� ��Ʈ
            
            //�������ϴ��� �� �հ� ����
            
            //����Ǵ� ��Ʈ��� ������
            xlsArea.applyAt(new CellRef("Template!A1"), context);
            xlsArea.processFormulas();    
            
            //���ø� ��Ʈ ����
            transformer.deleteSheet("Template");
            transformer.deleteSheet("Total");
            
            //���� ���� ��������
            transformer.write();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}