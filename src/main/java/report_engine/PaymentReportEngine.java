package report_engine;

import database.CompanyDataProcessor;
import deserialization.pojo.company.Company;
import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import report_engine.reports.ReportConstants;
import report_engine.reports.ReportData;

import java.util.Arrays;
import java.util.List;

public class PaymentReportEngine implements ReportEngine {

    private CompanyDataProcessor processor;
    private String aggregationType = "";

    public PaymentReportEngine(CompanyDataProcessor processor, String aggregation) {
        this.processor = processor;
        this.aggregationType = aggregation;
    }

    public PaymentReportEngine(CompanyDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public List<ReportData> createMonthlyReport(Company company, int month, int year, boolean desc, int limit) {

        List<Invoice> cashInvoices = processor.getInvoiceService().findAllByCompanyAndMonth(company.getId(), month, year,
                ReportConstants.CASH_AGGREGATION);
        List<Invoice> cardInvoices = processor.getInvoiceService().findAllByCompanyAndMonth(company.getId(), month, year,
                ReportConstants.CARD_AGGREGATION);
        List<Receipt> cashReceipts = processor.getReceiptService().findAllByCompanyAndMonth(company.getId(), month, year,
                ReportConstants.CASH_AGGREGATION);
        List<Receipt> cardReceipts = processor.getReceiptService().findAllByCompanyAndMonth(company.getId(), month, year,
                ReportConstants.CARD_AGGREGATION);

        ReportData cashData = new ReportData(ReportConstants.MONTHLY_REPORT,
                "CASH",
                cashInvoices, cashReceipts);
        ReportData cardData = new ReportData(ReportConstants.MONTHLY_REPORT,
                "CARD",
                cardInvoices, cardReceipts);

        return Arrays.asList(cardData,cashData);

    }

    @Override
    public List<ReportData> createQuarterlyReport(Company company, int quarter, int year, boolean desc, int limit) {

        List<Invoice> cashInvoices = processor.getInvoiceService().findAllByCompanyAndQuarter(company.getId(), quarter, year,
                ReportConstants.CASH_AGGREGATION);
        List<Invoice> cardInvoices = processor.getInvoiceService().findAllByCompanyAndQuarter(company.getId(), quarter, year,
                ReportConstants.CARD_AGGREGATION);
        List<Receipt> cashReceipts = processor.getReceiptService().findAllByCompanyAndQuarter(company.getId(), quarter, year,
                ReportConstants.CASH_AGGREGATION);
        List<Receipt> cardReceipts = processor.getReceiptService().findAllByCompanyAndQuarter(company.getId(), quarter, year,
                ReportConstants.CARD_AGGREGATION);

        ReportData cashData = new ReportData(ReportConstants.QUARTERLY_REPORT,
                "CASH",
                cashInvoices, cashReceipts);
        ReportData cardData = new ReportData(ReportConstants.QUARTERLY_REPORT,
                "CARD",
                cardInvoices, cardReceipts);

        return Arrays.asList(cardData,cashData);
    }

    @Override
    public List<ReportData> createYearlyReport(Company company, int year, boolean desc, int limit) {

        List<Invoice> cashInvoices = processor.getInvoiceService().findAllByCompanyAndYear(company.getId(), year,
                ReportConstants.CASH_AGGREGATION);
        List<Invoice> cardInvoices = processor.getInvoiceService().findAllByCompanyAndYear(company.getId(),  year,
                ReportConstants.CARD_AGGREGATION);
        List<Receipt> cashReceipts = processor.getReceiptService().findAllByCompanyAndYear(company.getId(),  year,
                ReportConstants.CASH_AGGREGATION);
        List<Receipt> cardReceipts = processor.getReceiptService().findAllByCompanyAndYear(company.getId(),  year,
                ReportConstants.CARD_AGGREGATION);

        ReportData cashData = new ReportData(ReportConstants.QUARTERLY_REPORT,
                "CASH",
                cashInvoices, cashReceipts);
        ReportData cardData = new ReportData(ReportConstants.QUARTERLY_REPORT,
                "CARD",
                cardInvoices, cardReceipts);

        return Arrays.asList(cardData,cashData);
    }

}
