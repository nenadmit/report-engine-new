package report_engine;

import database.CompanyDataProcessor;
import deserialization.pojo.company.Company;
import deserialization.pojo.company.Invoice;
import report_engine.reports.ReportData;
import report_engine.reports.ReportConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvoiceReportEngine implements ReportEngine {

    private CompanyDataProcessor processor;

    public InvoiceReportEngine(CompanyDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public List<ReportData> createMonthlyReport(Company company, int month, int year, boolean desc, int limit) {

        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndMonth(company.getId(), month, year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.MONTHLY_REPORT,
                "INVOICE",
                invoices,
                new ArrayList<>()));
    }

    @Override
    public List<ReportData> createQuarterlyReport(Company company, int quarter, int year, boolean desc, int limit) {

        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndQuarter(company.getId(), quarter, year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.QUARTERLY_REPORT,
                "INVOICE",
                invoices,
                new ArrayList<>()));
    }

    @Override
    public List<ReportData> createYearlyReport(Company company, int year, boolean desc, int limit) {

        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndYear(company.getId(), year,"ALL");

        return Arrays.asList(new ReportData(
                ReportConstants.YEARLY_REPORT,
                "INVOICE",
                invoices,
                new ArrayList<>()));
    }
}
