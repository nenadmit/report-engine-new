package database.service.implementation;

import database.repositoryy.Repository;
import database.service.interfaces.InvoiceService;
import database.specification.*;
import deserialization.pojo.company.Invoice;
import report_engine.reports.ReportConstants;

import java.util.List;

public class InvoiceServiceImpl extends GenericService<Invoice> implements InvoiceService {

    private Repository<Invoice> repository;

    public InvoiceServiceImpl(Repository<Invoice> repository) {
        super(repository, Invoice.class);
        this.repository = repository;
    }

    @Override
    public List<Invoice> getAllByStore(int storeId) {
        return repository.query(new FindBySingleFieldSpec<>(storeId,
                Invoice.Fields.TABLE.getValue(),
                Invoice.Fields.FK_INVOICE_STORE.getValue()));
    }

    @Override
    public List<Invoice> saveAll(List<Invoice> invoices) {
        return repository.createAll(invoices);
    }


    @Override
    public List<Invoice> findAllByCompanyAndMonth(int companyId, int month, int year, String paymentType) {
        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndMonth<>(Invoice.class, companyId, month, year,
                    ReportConstants.CARD_AGGREGATION));
        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndMonth<>(Invoice.class, companyId, month, year,
                    ReportConstants.CASH_AGGREGATION));
        }

        return repository.query(new GetBillsByCompanyAndMonth<>(Invoice.class, companyId, month, year));
    }

    @Override
    public List<Invoice> findAllByCompanyAndQuarter(int companyId, int quarter, int year, String paymentType) {
        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {

            return repository.query(new GetBillsByCompanyAndQuarter<>(Invoice.class, companyId, quarter, year,
                    ReportConstants.CARD_AGGREGATION));
        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {

            return repository.query(new GetBillsByCompanyAndQuarter<>(Invoice.class, companyId, quarter, year,
                    ReportConstants.CASH_AGGREGATION));
        }
        return repository.query(new GetBillsByCompanyAndQuarter<>(Invoice.class, companyId, quarter, year));
    }

    @Override
    public List<Invoice> findAllByCompanyAndYear(int companyId, int year, String paymentType) {

        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndYear<>(Invoice.class, companyId, year,
                    ReportConstants.CARD_AGGREGATION));

        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndYear<>(Invoice.class, companyId, year,
                    ReportConstants.CASH_AGGREGATION));
        }
        return repository.query(new GetBillsByCompanyAndYear<>(Invoice.class, companyId, year));
    }


}
