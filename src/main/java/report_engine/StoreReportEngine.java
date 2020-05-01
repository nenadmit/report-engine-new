package report_engine;

import database.CompanyDataProcessor;
import deserialization.pojo.company.Company;
import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import deserialization.pojo.company.Store;
import report_engine.reports.ReportData;
import report_engine.reports.ReportConstants;

import java.util.*;

public class StoreReportEngine implements ReportEngine {

    private CompanyDataProcessor processor;
    private List<ReportData> reports = new ArrayList<>();

    public StoreReportEngine(CompanyDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public List<ReportData> createMonthlyReport(Company company, int month, int year, boolean desc, int limit) {

        Map<Integer, Store> storeMap = getStoresMap(processor.getStoreService().findByCompany(company.getId()));
        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndMonth(company.getId(), month, year,"ALL");
        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndMonth(company.getId(), month, year,"ALL");

        storeMap = setInvoicesAndReceipts(storeMap, invoices, receipts);

        for (Store store : storeMap.values()) {

            reports.add(new ReportData(ReportConstants.MONTHLY_REPORT,
                    store.getName(),
                    store.getInvoices(),
                    store.getReceipts()));
        }

        return reports;
    }

    @Override
    public List<ReportData> createQuarterlyReport(Company company, int quarter, int year, boolean desc, int limit) {

        Map<Integer, Store> storeMap = getStoresMap(processor.getStoreService().findByCompany(company.getId()));
        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndQuarter(company.getId(), quarter, year,"ALL");
        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndQuarter(company.getId(), quarter, year,"ALL");

        storeMap = setInvoicesAndReceipts(storeMap, invoices, receipts);

        for (Store store : storeMap.values()) {

            reports.add(new ReportData(ReportConstants.QUARTERLY_REPORT,
                    store.getName(),
                    store.getInvoices(),
                    store.getReceipts()));
        }

        Collections.sort(reports);
        if (!desc) {
            Collections.reverse(reports);
        }

        if (limit != 0 && limit <= reports.size()) {
            return reports.subList(0, limit);
        }
        return reports;
    }

    @Override
    public List<ReportData> createYearlyReport(Company company, int year, boolean desc, int limit) {

        Map<Integer, Store> storeMap = getStoresMap(processor.getStoreService().findByCompany(company.getId()));
        List<Invoice> invoices = processor.getInvoiceService().findAllByCompanyAndYear(company.getId(), year,"ALL");
        List<Receipt> receipts = processor.getReceiptService().findAllByCompanyAndYear(company.getId(), year,"ALL");

        storeMap = setInvoicesAndReceipts(storeMap, invoices, receipts);

        for (Store store : storeMap.values()) {

            reports.add(new ReportData(ReportConstants.YEARLY_REPORT,
                    store.getName(),
                    store.getInvoices(),
                    store.getReceipts()));
        }

        return reports;
    }

    public Map<Integer, Store> getStoresMap(List<Store> stores) {

        Map<Integer, Store> storeMap = new LinkedHashMap<>();
        stores.stream().forEach(store -> storeMap.put(store.getId(), store));

        return storeMap;
    }

    public Map<Integer, Store> setInvoicesAndReceipts(Map<Integer, Store> map,
                                                      List<Invoice> invoices,
                                                      List<Receipt> receipts) {

        for (Invoice invoice : invoices) {
            map.get(invoice.getStoreId()).getInvoices().add(invoice);
        }
        for (Receipt receipt : receipts) {
            map.get(receipt.getStoreId()).getReceipts().add(receipt);
        }
        return map;

    }
}
