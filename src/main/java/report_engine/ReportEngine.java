package report_engine;

import deserialization.pojo.company.Company;
import report_engine.reports.ReportData;

import java.util.List;

public interface ReportEngine {

    List<ReportData> createMonthlyReport(Company company, int month, int year, boolean desc, int limit);
    List<ReportData> createQuarterlyReport(Company company,int quarter,int year,boolean desc,int limit);
    List<ReportData> createYearlyReport(Company company, int year,boolean desc,int limit);

}
