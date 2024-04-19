package hn.lacolonia.services;

import java.io.File;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportGenerator {
	
	private String reportPath;
	
	public boolean generarReportePDF(String nombreReporte, Map<String, Object> parametros, JRDataSource datasource) {
		boolean generado = false;
		
		try {
			JasperReport reporte = (JasperReport)JRLoader.loadObjectFromFile(obtenerUbicacionArchivo(nombreReporte + ".jasper"));
			JasperPrint impresora = JasperFillManager.fillReport(reporte, parametros, datasource);
			String rutaPDF = generarUbicacionArchivo();
			reportPath = rutaPDF;
			JasperExportManager.exportReportToPdfFile(impresora, rutaPDF);
			generado = true;
		}catch(Exception error) {
			error.printStackTrace();
			generado = false;
		}
		
		return generado;
	}

	private String generarUbicacionArchivo() {
		String path = null;
		try {
			path = File.createTempFile("temp", ".pdf").getAbsolutePath();
		}catch(Exception error) {
			error.printStackTrace();
		}
		return path;
	}

	private String obtenerUbicacionArchivo(String archivo) {
		String path = null;
		try {
			path = ResourceUtils.getFile("classpath:"+archivo).getAbsolutePath();
		}catch(Exception error) {
			error.printStackTrace();
		}
		return path;
	}
	
	public String getReportPath() {
		return this.reportPath;
	}
}
