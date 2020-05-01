package database.service.interfaces;

import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;

import java.util.List;

public interface  InvoiceService extends Service<Invoice> {

    List<Invoice> getAllByStore(int storeId);
    List<Invoice> saveAll(List<Invoice> receipts);
    List<Invoice> findAllByCompanyAndMonth(int companyId,int month,int year, String paymentType);
    List<Invoice> findAllByCompanyAndQuarter(int companyId,int quarter,int year, String paymentType);
    List<Invoice> findAllByCompanyAndYear(int companyId,int year, String paymentType);
}
