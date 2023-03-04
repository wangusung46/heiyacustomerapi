package com.heiya.mobileapi.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.filechooser.FileSystemView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportUtils extends AbstractCommon {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtils.class);

    @Value("${report.path.jasper}")
    private String pathJasper;

    @Value("${report.path.result}")
    private String pathResult;

    @Autowired
    private DataSource dataSource;

    private void showFilePathWorkingDirectory() {
        LOGGER.info("\n\n======== SHOW ReportUtils.setFilePathWorkingDirectory " + System.getProperty("user.dir"));
    }

    public String exportPdf(String paramFileName, Map<String, Object> param) throws Exception {

        String path = pathResult;
        createFolder(path);

        JasperReport jasperReport = JasperCompileManager.compileReport(pathJasper + paramFileName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource.getConnection());

        String fileName = paramFileName.substring(0, paramFileName.lastIndexOf('.'));
        fileName = fileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime());
        fileName = path + fileName.concat(".pdf");

        JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
        return fileName;
    }

    public String exportHtml(String paramFileName, Map<String, Object> param) throws Exception {

        showFilePathWorkingDirectory();
        String path = pathResult;
        createFolder(path);

        JasperReport jasperReport = JasperCompileManager.compileReport(pathJasper + paramFileName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource.getConnection());

        String fileName = paramFileName.substring(0, paramFileName.lastIndexOf('.'));
        fileName = fileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime());
        fileName = path + fileName.concat(".html");
        JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName);
        String content = new String(Files.readAllBytes(Paths.get(fileName)));

        return content;
    }

    public void compileReport(String paramFileName) throws JRException {

        JasperCompileManager.compileReportToFile(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + pathJasper + paramFileName, FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + pathJasper + paramFileName.substring(0, paramFileName.lastIndexOf('.')) + ".jasper");
    }

    public String exportExcel(String paramFileName, Map<String, Object> param) throws Exception {

        String path = pathResult;
        createFolder(path);

        JasperReport jasperReport = JasperCompileManager.compileReport(pathJasper + paramFileName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource.getConnection());

        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setRemoveEmptySpaceBetweenColumns(true);
        reportConfigXLS.setRemoveEmptySpaceBetweenRows(true);
        reportConfigXLS.setImageBorderFixEnabled(true);
        reportConfigXLS.setDetectCellType(true);
        reportConfigXLS.setWrapText(true);
        reportConfigXLS.setWhitePageBackground(false);

        String fileName = paramFileName.substring(0, paramFileName.lastIndexOf('.'));
        fileName = fileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime());
        fileName = path + fileName.concat(".xls");
        JRXlsExporter exporter = new JRXlsExporter();

        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));
        exporter.exportReport();

        return fileName;
    }

    public String exportCsv(String paramFileName, Map<String, Object> param) throws Exception {

        String path = pathResult;
        createFolder(path);

        JasperReport jasperReport = JasperCompileManager.compileReport(pathJasper + paramFileName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource.getConnection());

        String fileName = paramFileName.substring(0, paramFileName.lastIndexOf('.'));
        fileName = fileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime());
        fileName = path + fileName.concat(".csv");

        SimpleCsvMetadataExporterConfiguration configuration = new SimpleCsvMetadataExporterConfiguration();

        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(new File(fileName)));
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return fileName;
    }

    public String exportDocx(String paramFileName, Map<String, Object> param) throws Exception {

        String path = pathResult;
        createFolder(path);

        JasperReport jasperReport = JasperCompileManager.compileReport(pathJasper + paramFileName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource.getConnection());

        String fileName = paramFileName.substring(0, paramFileName.lastIndexOf('.'));
        fileName = fileName + "_" + formatTimestamp.format(Calendar.getInstance().getTime());
        fileName = path + fileName.concat(".docx");

        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));
        exporter.exportReport();

        return fileName;
    }

    private void createFolder(String p_Path) {
        File directory = new File(p_Path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

}
