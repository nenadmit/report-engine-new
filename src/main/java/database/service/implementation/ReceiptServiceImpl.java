package database.service.implementation;

import database.repositoryy.Repository;
import database.service.interfaces.ReceiptService;
import database.specification.*;
import deserialization.pojo.company.Receipt;
import report_engine.reports.ReportConstants;

import java.util.List;

public class ReceiptServiceImpl extends GenericService<Receipt> implements ReceiptService {

    private Repository<Receipt> repository;

    public ReceiptServiceImpl(Repository<Receipt> repository) {
        super(repository, Receipt.class);
        this.repository = repository;
    }


    @Override
    public List<Receipt> getAllByStore(int storeId) {
        return repository.query(new FindBySingleFieldSpec<>(storeId,
                Receipt.Fields.TABLE.getValue(),
                Receipt.Fields.FK_RECEIPT_STORE.getValue()));
    }


    @Override
    public List<Receipt> saveAll(List<Receipt> receipts) {
        return repository.createAll(receipts);
    }


    @Override
    public List<Receipt> findAllByCompanyAndMonth(int companyId, int month, int year, String paymentType) {

        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndMonth<>(Receipt.class, companyId, month, year,
                    ReportConstants.CARD_AGGREGATION));

        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndMonth<>(Receipt.class, companyId, month, year,
                    ReportConstants.CASH_AGGREGATION));
        }
        return repository.query(new GetBillsByCompanyAndMonth<>(Receipt.class, companyId, month, year));
    }

    @Override
    public List<Receipt> findAllByCompanyAndQuarter(int companyId, int quarter, int year, String paymentType) {

        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndQuarter<>(Receipt.class, companyId, quarter, year,
                    ReportConstants.CARD_AGGREGATION));

        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndQuarter<>(Receipt.class, companyId, quarter, year,
                    ReportConstants.CASH_AGGREGATION));
        }

        return repository.query(new GetBillsByCompanyAndQuarter<>(Receipt.class, companyId, quarter, year));
    }

    @Override
    public List<Receipt> findAllByCompanyAndYear(int companyId, int year, String paymentType) {

        if (paymentType.equals(ReportConstants.CARD_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndYear<>(Receipt.class, companyId, year,
                    ReportConstants.CARD_AGGREGATION));

        } else if (paymentType.equals(ReportConstants.CASH_AGGREGATION)) {
            return repository.query(new GetBillsByCompanyAndYear<>(Receipt.class, companyId, year,
                    ReportConstants.CASH_AGGREGATION));
        }
        return repository.query(new GetBillsByCompanyAndYear<>(Receipt.class, companyId, year));
    }

}
