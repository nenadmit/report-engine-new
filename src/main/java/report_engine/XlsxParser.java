package report_engine;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import report_engine.reports.ReportData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XlsxParser {

    public void exportAsXlsx(List<ReportData> reports, String filename) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("test");

        int rowCounter=0;
        int cellCounter=0;


        Row row = sheet.createRow(rowCounter++);

        row.createCell(cellCounter++).setCellValue("STORES");
        for(String field:reports.get(0).getTurnoverPerTimePeriod().keySet())
            row.createCell(cellCounter++).setCellValue(field);
        row.createCell(cellCounter).setCellValue("TURNOVER");

        cellCounter = 0;
        for(ReportData data:reports){
            row = sheet.createRow(rowCounter++);
            row.createCell(cellCounter++).setCellValue(data.getAggregationName());

            for(Double turnover:data.getTurnoverPerTimePeriod().values()){

                row.createCell(cellCounter++).setCellValue(turnover);
            }
            row.createCell(cellCounter++).setCellValue(data.getTotal());
            cellCounter = 0;
        }

        workbook.write(new FileOutputStream(new File("export/"+filename+".xlsx")));
        workbook.close();
    }
}
