package com.servlet;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class ExportData
 */
public class ExportData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Pragma", "");
		response.setHeader("Expires","");
		response.setHeader("Cache-Control", "");
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition","filename=Export.xls");	
		
		String filename = System.getProperty("java.io.tmpdir") + "/export.xls";
		File f = new File(System.getProperty("java.io.tmpdir") + "/export.xls");

		InputStream input = getServletContext().getResourceAsStream("Export_DA.xls");
		
		FileOutputStream fileOut=  new FileOutputStream(filename);

			/*int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				fileOut.write(bytes, 0, read);
			}
			fileOut.flush();
	      */ 
	       
		// ----------------------------------------------------------
	       HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("new sheet");
			

			/* Create JFreeChart object that will hold the Pie Chart Data */
            DefaultPieDataset my_pie_chart_data = new DefaultPieDataset();
            my_pie_chart_data.setValue("walid",4);
            my_pie_chart_data.setValue("farhat",4);
            my_pie_chart_data.setValue("hedhi",7);
            my_pie_chart_data.setValue("mohamed",11);
            /* Create a logical chart object with the chart data collected */
            JFreeChart myPieChart=ChartFactory.createPieChart("",my_pie_chart_data,true,true,false);
            myPieChart.getLegend().setHorizontalAlignment(HorizontalAlignment.LEFT);
            myPieChart.getLegend().setVerticalAlignment(VerticalAlignment.CENTER);
            myPieChart.getLegend().setPosition(RectangleEdge.LEFT);
            myPieChart.setBackgroundPaint(Color.white);
            myPieChart.setBorderPaint(Color.gray);
            myPieChart.getLegend().setBorder(0, 0, 0, 0);
            myPieChart.getPlot().setBackgroundPaint(Color.white);
            myPieChart.getPlot().setOutlineVisible(false);
            PiePlot p = (PiePlot) myPieChart.getPlot();
            p.setLegendItemShape(new Rectangle(10,10));
            p.setLabelGenerator(null);
            
            p.setSectionPaint("walid", new Color(209,209,209));
            p.setSectionPaint("farhat", Color.red);
            p.setSectionPaint("hedhi",  new Color(0,230,0));
            p.setSectionPaint("mohamed", new Color(0,80,0));
            
            /*
             * todo
             */
            
            myPieChart.setBorderVisible(true);
            
            /* Specify the height and width of the Pie Chart */
            int width=930; 
            int height=188;
            float quality=1; 
            ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
            ChartUtilities.writeChartAsJPEG(chart_out,quality,myPieChart,width,height);
            /* We now read from the output stream and frame the input chart data */
            InputStream feed_chart_to_excel=new ByteArrayInputStream(chart_out.toByteArray());
            byte[] bytes = IOUtils.toByteArray(feed_chart_to_excel);
            
            /* Add picture to workbook */
            /* Add picture to workbook */
            int my_picture_id = hwb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            /* We can close Piped Input Stream. We don't need this */
            feed_chart_to_excel.close();
            /* Close PipedOutputStream also */
            chart_out.close();
            
            
            /* Create an anchor point */
            HSSFPatriarch drawing = sheet.createDrawingPatriarch();            
            /* Define top left corner, and we can resize picture suitable from there */
            
            ClientAnchor my_anchor = new HSSFClientAnchor();
            my_anchor.setCol1(0);
            my_anchor.setRow1(1);
            HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
            /* Call resize method, which resizes the image */
            my_picture.resize();
		
			
			HSSFRow rowhead=   sheet.createRow(50);
			
		    rowhead.createCell(0).setCellValue("walid");
				
			 
			hwb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
		ServletOutputStream op       = response.getOutputStream();

	       
        if (f.isFile()) {
        response.setContentLength( (int)f.length() );       
        BufferedInputStream fif= new BufferedInputStream(new FileInputStream(f));
        // copie le fichier dans le flux de sortie
	      int data;
	      while((data = fif.read())!=-1) {
	    	  op.write(data);
	      }
	      fif.close();
	      op.close();
	      
		}
        else response.sendError(404);
	}

}
