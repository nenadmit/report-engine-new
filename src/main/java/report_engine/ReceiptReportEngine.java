package report_engine;

import database.CompanyDataProcessor;
import deserialization.pojo.company.Company;
import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import report_engine.reports.ReportData;
import report_engine.reports.ReportConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiptReportEngine implements ReportEngine {

    private CompanyDataProcessor processor;

    public ReceiptReportEngine(CompanyDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public List<ReportData> createMonthlyReport(Company company, int month, int year, boolean desc, int limit) {

        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndMonth(company.getId(), month, year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.MONTHLY_REPORT,
                "RECEIPT",
                new ArrayList<>(),
                receipts));
    }

    @Override
    public List<ReportData> createQuarterlyReport(Company company, int quarter, int year, boolean desc, int limit) {

        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndQuarter(company.getId(), quarter, year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.QUARTERLY_REPORT,
                "RECEIPT",
                new ArrayList<>(),
                receipts));
    }

    @Override
    public List<ReportData> createYearlyReport(Company company, int year, boolean desc, int limit) {

        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndYear(company.getId(), year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.YEARLY_REPORT,
                "RECEIPT",
                new ArrayList<>(),
                receipts));
    }
}
