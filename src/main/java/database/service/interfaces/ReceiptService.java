package database.service.interfaces;

import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;


import java.util.List;

public interface ReceiptService extends Service<Receipt> {


    List<Receipt> getAllByStore(int storeId);
    List<Receipt> saveAll(List<Receipt> receipts);
    List<Receipt> findAllByCompanyAndMonth(int companyId,int month,int year,String paymentType);
    List<Receipt> findAllByCompanyAndQuarter(int companyId,int quarter,int year,String paymentType);
    List<Receipt> findAllByCompanyAndYear(int companyId,int year,String paymentType);
}