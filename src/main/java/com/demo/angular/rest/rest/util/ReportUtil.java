package com.demo.angular.rest.rest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

public class ReportUtil {

	public static byte[] getItemReport(List<?> items, String format,String path,String jasper) {
		JasperReport jasperReport;
	
		try {			
			jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile(jasper));
		} catch (FileNotFoundException | JRException e) {
			try {

				File file = ResourceUtils.getFile(path);
				jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
				JRSaver.saveObject(jasperReport, jasper);
			} catch (FileNotFoundException | JRException ex) {
				throw new RuntimeException(e);
			}
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("title", "Item Report");
		JasperPrint jasperPrint = null;
		byte[] reportContent;

		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			switch (format) {
			case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
			case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
			default -> throw new RuntimeException("Unknown report format");
			}
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
		return reportContent;
	}
}
