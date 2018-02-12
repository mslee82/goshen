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
    
    public void multiSheetDownload(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 

        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //���ø� ��� ����
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            // Create a top level area
            XlsArea xlsArea = new XlsArea("Template!A1:K15", transformer);		//���ø� ���� ����  Template!A1:K15 ��Ʈ��!������:������
            
            // Create a receipt level area
            XlsArea receiptArea = new XlsArea("Template!A1:K15", transformer);	//����Ǵ� ������ ���� ���� �ݺ��Ǵ� ���ΰ�??? �׷��ٸ� ������ ����
            
            //multi sheet ó��. SimpleCellRefGenerator class���� ó���ϴ�  CellRef("sheet" + index + "!B2"); �� �κп��� sheet name�� �����Ѵ�.
            // �ݺ� ���� ����
            EachCommand receiptEachCommand = new EachCommand("data", "dataList", receiptArea, new SimpleCellRefGenerator());
            
            //���ҿ����� �ʿ��ϴٸ� �̷���.... ���� ���⿡ ����� ȸ�������� �ְ��� 
            XlsArea companyArea = new XlsArea("Template!A9:F9", transformer);
            //XlsArea ifArea = new XlsArea("Template!A18:F18", transformer);
            
            //���⸦ �̿��ؼ� ���ΰ� ���� ����ϵ��� ó���Ѵ�.
            //IfCommand ifCommand = new IfCommand("employee.payment <= 2000", ifArea, new XlsArea("Template!A9:F9", transformer));
            
            //employeeArea.addCommand(new AreaRef("Template!A9:F9"), ifCommand);
            //Command companyEachCommand = new EachCommand("employee", "department.staff", companyArea);
            
            //receiptArea.addCommand(new AreaRef("Template!A9:F9"), companyEachCommand);
            
            //xlsArea.addCommand(new AreaRef("Template!A1:K15"), receiptEachCommand); //��Ʈ�� �������� ū ����... ���� �̰� ���� Ȥ�� ������, �������� ��Ÿ��. Ȥ�� ������ ���� 
           
            //������ ǥ���Ǵ� �����
            Context context = new Context();
            context.putVar("dataList", bean.get("dataList"));		//���� Map, List�� context�� ��´�.
            //context.putVar("", value);
            //context.putVar("departments", departments);
            
            xlsArea.applyAt(new CellRef("Sheet!A1"), context);
            xlsArea.processFormulas();
            transformer.write();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //�鼼�и�, ��������, �� ����
    public void multiSheetReceiptDownloadBak(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 
    	
        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //���ø� ��� ����
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            //��ü ���ø� ���� ����  Template!A1:K15 -> ��Ʈ��!������:������
            XlsArea xlsArea = new XlsArea("Template!A1:L17", transformer);
            
            //
            XlsArea sheetArea = new XlsArea("Template!A2:L16", transformer);
            
            //multi sheet command �ܼ��� List�� ǥ���ϱ� ���ؼ���� �̰��� ����� �ʿ䰡 ����. ���� ��� ��,�Ϻ��� ��Ʈ�� �����Ƿ� �̰��� ���ڸ� �������� �ݺ���.
            //1,2 �׸��� �������� �鼼�и��� �� ���� ����Ʈ��, ����������� �� ���� ����Ʈ��, �������̶�� �պκ��� ������ �������� ��ŭ�� �׶��� �̺κ��� selldt��? 
            EachCommand sheetEachCommand = new EachCommand("sheetList", "sheetLists", sheetArea, new SimpleCellRefGenerator());
            
            //������ ����� ������ ������. 
            XlsArea employeeArea = new XlsArea("Template!A13:K13", transformer);
            XlsArea ifArea = new XlsArea("Template!A18:K18", transformer);
            IfCommand ifCommand = new IfCommand("employee.payment <= 2000",
                    ifArea,
                    new XlsArea("Template!A13:K13", transformer));
            employeeArea.addCommand(new AreaRef("Template!A13:K13"), ifCommand);
            
            //������ ��� �����ͼ��� ����
            Command receiptEachCommand = new EachCommand("receiptData", "sheetList.receiptData", employeeArea);
            sheetArea.addCommand(new AreaRef("Template!A13:K13"), receiptEachCommand);
            
            
            xlsArea.addCommand(new AreaRef("Template!A2:K16"), sheetEachCommand); //multi sheet command�� ������ �߰���.
            
            /*Command receiptEachCommand = new EachCommand("receipt", "receipt", employeeArea);
            departmentArea.addCommand(new AreaRef("Template!A9:F9"), employeeEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:F12"), departmentEachCommand);
            */
            //�����ͼ��� ���⿡ putVar�� �����ؼ� ���
            Context context = new Context();
            context.putVar("sheetLists", bean.get("sheetList"));		
            context.putVar("dataList", bean.get("dataList"));
            
            //����Ǵ� ��Ʈ��� ������
            xlsArea.applyAt(new CellRef("Sheet!A1"), context);
            xlsArea.processFormulas();
            transformer.write();
            
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
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            //��ü ���ø� ���� ����  Template!A1:K15 -> ��Ʈ��!������:������
            XlsArea xlsArea = new XlsArea("Template!A1:L19", transformer);
            
            //
            XlsArea sheetArea = new XlsArea("Template!A2:L18", transformer);
            
            //multi sheet command �ܼ��� List�� ǥ���ϱ� ���ؼ���� �̰��� ����� �ʿ䰡 ����. ���� ��� ��,�Ϻ��� ��Ʈ�� �����Ƿ� �̰��� ���ڸ� �������� �ݺ���.
            //1,2 �׸��� �������� �鼼�и��� �� ���� ����Ʈ��, ����������� �� ���� ����Ʈ��, �������̶�� �պκ��� ������ �������� ��ŭ�� �׶��� �̺κ��� selldt��? 
            EachCommand sheetEachCommand = new EachCommand("sheetList", "sheetLists", sheetArea, new SimpleCellRefGenerator());
            
            //������ ����� ������ ������. 
            XlsArea excelArea = new XlsArea("Template!A14:K14", transformer);
            /* 
            XlsArea ifArea = new XlsArea("Template!A14:K14", transformer);
            IfCommand ifCommand = new IfCommand("receiptData.cust_no > 0",
                    ifArea,
                    new XlsArea("Template!A14:K14", transformer));
            excelArea.addCommand(new AreaRef("Template!A14:K14"), ifCommand);
            */
            
            //������ ��� �����ͼ��� ����
            Command receiptEachCommand = new EachCommand("receiptData", "sheetList.receiptData", excelArea);
            sheetArea.addCommand(new AreaRef("Template!A14:K14"), receiptEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:K18"), sheetEachCommand); //multi sheet command�� ������ �߰���.
            
            //�����ͼ��� ���⿡ putVar�� �����ؼ� ���
            Context context = new Context();
            context.putVar("sheetLists", bean.get("sheetList"));	//��Ʈ���� ���� �۾�. �Ϻ�, �������� ����, �鼼������
            context.putVar("dataList", bean.get("dataList"));		//���� ������ ����
            
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
    
    //sample
    public void multiSheetDownloadDept(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 
    	List<Department> departments = EachIfCommandDemo.createDepartments();
        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //���ø� ��� ����
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            XlsArea xlsArea = new XlsArea("Template!A1:L15", transformer);
            XlsArea departmentArea = new XlsArea("Template!A2:L12", transformer);
            EachCommand departmentEachCommand = new EachCommand("department", "departments", departmentArea, new SimpleCellRefGenerator());
            XlsArea employeeArea = new XlsArea("Template!A8:K8", transformer);
            XlsArea ifArea = new XlsArea("Template!A18:K18", transformer);
            IfCommand ifCommand = new IfCommand("employee.payment <= 2000",
                    ifArea,
                    new XlsArea("Template!A8:K8", transformer));
            employeeArea.addCommand(new AreaRef("Template!A8:K8"), ifCommand);
            Command employeeEachCommand = new EachCommand("dataList", "dataList", employeeArea);
            departmentArea.addCommand(new AreaRef("Template!A8:K8"), employeeEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:K12"), departmentEachCommand);
            
            /*Command receiptEachCommand = new EachCommand("receipt", "receipt", employeeArea);
            departmentArea.addCommand(new AreaRef("Template!A9:F9"), employeeEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:F12"), departmentEachCommand);
            */
            Context context = new Context();
            context.putVar("departments", departments);		//map, list�� ���⿡ putVar��
            context.putVar("dataList", bean.get("dataList"));		//map, list�� ���⿡ putVar�� 
            xlsArea.applyAt(new CellRef("Sheet!A1"), context);
            xlsArea.processFormulas();
            transformer.write();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //sample
    public void multiSheetDownloadDeptBak(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> bean, String fileName, String templateFile, String string)
                    throws ParsePropertyException, InvalidFormatException { 
    	List<Department> departments = EachIfCommandDemo.createDepartments();
        String tempPath = "C:\\work";
        try {
        	InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile)); //���ø� ��� ����
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        	OutputStream os =  response.getOutputStream(); //new FileOutputStream(fileName); 			   //����Ǵ� ���ϸ�
        	
        	Transformer transformer = TransformerFactory.createTransformer(is, os);
            System.out.println("Creating area");
            
            XlsArea xlsArea = new XlsArea("Template!A1:G15", transformer);
            XlsArea departmentArea = new XlsArea("Template!A2:G12", transformer);
            EachCommand departmentEachCommand = new EachCommand("department", "departments", departmentArea, new SimpleCellRefGenerator());
            XlsArea employeeArea = new XlsArea("Template!A8:F8", transformer);
            XlsArea ifArea = new XlsArea("Template!A18:F18", transformer);
            IfCommand ifCommand = new IfCommand("employee.payment <= 2000",
                    ifArea,
                    new XlsArea("Template!A8:F8", transformer));
            employeeArea.addCommand(new AreaRef("Template!A8:F8"), ifCommand);
            Command employeeEachCommand = new EachCommand("dataList", "dataList", employeeArea);
            departmentArea.addCommand(new AreaRef("Template!A8:F8"), employeeEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:F12"), departmentEachCommand);
            
            /*Command receiptEachCommand = new EachCommand("receipt", "receipt", employeeArea);
            departmentArea.addCommand(new AreaRef("Template!A9:F9"), employeeEachCommand);
            xlsArea.addCommand(new AreaRef("Template!A2:F12"), departmentEachCommand);
            */
            Context context = new Context();
            context.putVar("departments", departments);		//map, list�� ���⿡ putVar��
            context.putVar("dataList", bean.get("dataList"));		//map, list�� ���⿡ putVar�� 
            xlsArea.applyAt(new CellRef("Sheet!A1"), context);
            xlsArea.processFormulas();
            transformer.write();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}