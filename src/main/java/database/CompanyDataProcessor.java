package database;

import database.service.implementation.XmlCompanyFileService;
import database.service.interfaces.*;

public class CompanyDataProcessor {

    private CompanyService companyService;
    private StoreService storeService;
    private CustomerService customerService;
    private CardService cardService;
    private ReceiptService receiptService;
    private InvoiceService invoiceService;
    private XmlCompanyFileService xmlCompanyFileService;

    public CompanyDataProcessor(CompanyService companyService,
                                StoreService storeService,
                                CustomerService customerService,
                                CardService cardService,
                                ReceiptService receiptService,
                                InvoiceService invoiceService,
                                XmlCompanyFileService xmlCompanyFileService) {
        this.companyService = companyService;
        this.storeService = storeService;
        this.customerService = customerService;
        this.cardService = cardService;
        this.receiptService = receiptService;
        this.invoiceService = invoiceService;
        this.xmlCompanyFileService = xmlCompanyFileService;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public CardService getCardService() {
        return cardService;
    }

    public ReceiptService getReceiptService() {
        return receiptService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public XmlCompanyFileService getXmlCompanyFileService() {
        return xmlCompanyFileService;
    }
}
