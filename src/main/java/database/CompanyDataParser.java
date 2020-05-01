package database;

import deserialization.XmlParser;
import deserialization.pojo.company.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompanyDataParser {

    private CompanyDataProcessor processor;
    private XmlParser<Company> xmlParser;

    private Map<Long, Company> unsavedCompanies = new HashMap<>();
    private Map<String, Store> unsavedStores = new HashMap<>();
    private Map<Long, Card> unsavedCards = new HashMap<>();
    private Map<Long, Customer> unsavedCustomers = new HashMap<>();
    private List<Receipt> receipts = new LinkedList<>();
    private List<Invoice> invoices = new LinkedList<>();

    public CompanyDataParser(CompanyDataProcessor processor, XmlParser<Company> xmlParser) {
        this.processor = processor;
        this.xmlParser = xmlParser;
    }


    public List<Company> parseAll(File[] files){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Map<String,Date> parsedFiles = getXmlReportsAsMap(processor.getXmlCompanyFileService().findAll());
        List<XmlCompanyReport> unsavedFiles = new ArrayList<>();
        List<Company> companies = new ArrayList<>();

        for(File file:files){

            if(!parsedFiles.containsKey(file.getName())){

                companies.add(xmlParser.parse(file));
                try {
                    unsavedFiles.add(new XmlCompanyReport(file.getName(),sdf.parse(file.getName())));
                    file.deleteOnExit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        System.out.println("Processed " + unsavedFiles.size() + " number of files!");

        processor.getXmlCompanyFileService().createAll(unsavedFiles);

        return companies;

    }

    public void saveAll(List<Company> companies) {

        setupCompanyData(companies);
        saveEntitiesWithoutForeignHey();
        handleEntitiesWithForeignKeys();

    }

    /**
     * Loads the already saved data in the database as a HashMap @fields savedEntity Map
     * Checks if the entity is already present, if not adds it to the unsavedEntity Map
     */

    private void setupCompanyData(List<Company> companies) {

        Map<Long, Company> savedCompanies = companiesMap();
        Map<String, Store> savedStores = storeMap();
        Map<Long, Customer> savedCustomers = customerMap();
        Map<Long, Card> savedCards = cardMap();

        for (Company company : companies) {

            if (!savedCompanies.containsKey(company.getUuid()) &&
                    !unsavedCompanies.containsKey(company.getUuid())) {
                unsavedCompanies.put(company.getUuid(), company);
            }

            for (Store store : company.getStores()) {

                if (!savedStores.containsKey(store.getName()) && !unsavedStores.containsKey(store.getName())) {
                    store.setCompany(company);
                    unsavedStores.put(store.getName(), store);
                }

                for (Receipt receipt : store.getReceipts()) {
                    receipt.setStore(store);

                    if (receipt.getPaymentType().equals("card")) {
                        Card card = receipt.getCard();
                        if (!savedCards.containsKey(card.getNumber()) && !unsavedCards.containsKey(card.getNumber()))
                            unsavedCards.put(card.getNumber(), card);

                    }
                    receipts.add(receipt);
                }

                for (Invoice invoice : store.getInvoices()) {
                    invoice.setStore(store);

                    if (invoice.getPaymentType().equals("card")) {
                        Card card = invoice.getCard();
                        if (!savedCards.containsKey(card.getNumber()) && !unsavedCards.containsKey(card.getNumber()))
                            unsavedCards.put(card.getNumber(), card);
                    }
                    Customer customer = invoice.getCustomer();
                    if (!savedCustomers.containsKey(customer.getUuid()) && !unsavedCustomers.containsKey(customer.getUuid()))
                        unsavedCustomers.put(customer.getUuid(), customer);

                    invoices.add(invoice);
                }
            }
        }
    }

    /**
     * Inserts entities without which don't contain foreign keys into a database
     *
     * @Company
     * @Card
     * @Customer
     */

    private void saveEntitiesWithoutForeignHey() {

        processor.getCompanyService().createAll(new LinkedList<>(unsavedCompanies.values()));
        processor.getCardService().createAll(new LinkedList<>(unsavedCards.values()));
        processor.getCustomerService().createAll(new LinkedList<>(unsavedCustomers.values()));

    }

    /**
     * Retreives parent entities from the database and uses id info to setup foreign key mapping
     * Inserts after foreign keys are inserted
     *
     * @Invoice and @Receipt are saved using separate threads.
     */

    private void handleEntitiesWithForeignKeys() {

        Map<Long, Company> savedCompanies = companiesMap();

        for (Map.Entry<String, Store> entry : unsavedStores.entrySet()) {

            Store store = entry.getValue();
            int companyId = savedCompanies.get(store.getCompany().getUuid()).getId();
            store.setCompanyId(companyId);
            entry.setValue(store);

        }

        processor.getStoreService().createAll(new LinkedList<>(unsavedStores.values()));

        Map<String, Store> savedStores = storeMap();
        Map<Long, Customer> savedCustomers = customerMap();
        Map<Long, Card> savedCards = cardMap();

        for (Receipt receipt : receipts) {

            receipt.setStoreId(savedStores.get(receipt.getStore().getName()).getId());

            if (receipt.getPaymentType().equals("card"))
                receipt.setCardId(savedCards.get(receipt.getCard().getNumber()).getId());
        }

        for (Invoice invoice : invoices) {

            invoice.setStoreId(savedStores.get(invoice.getStore().getName()).getId());
            if (invoice.getPaymentType().equals("card"))
                invoice.setCardId(savedCards.get(invoice.getCard().getNumber()).getId());

            invoice.setCustomerId(savedCustomers.get(invoice.getCustomer().getUuid()).getId());
        }

        processor.getReceiptService().createAll(receipts);
        processor.getInvoiceService().createAll(invoices);

    }

    private Map<Long, Company> companiesMap() {

        List<Company> savedCompanies = processor.getCompanyService().findAll();
        Map<Long, Company> companyMap = new LinkedHashMap<>();
        savedCompanies.stream().forEach(company -> companyMap.put(company.getUuid(), company));

        return companyMap;
    }

    private Map<String, Store> storeMap() {

        List<Store> stores = processor.getStoreService().findAll();
        Map<String, Store> storeMap = new LinkedHashMap<>();
        stores.forEach(store -> storeMap.put(store.getName(), store));

        return storeMap;
    }

    private Map<Long, Card> cardMap() {

        List<Card> cards = processor.getCardService().findAll();
        Map<Long, Card> cardMap = new LinkedHashMap<>();
        cards.forEach(card -> cardMap.put(card.getNumber(), card));

        return cardMap;
    }

    private Map<Long, Customer> customerMap() {

        List<Customer> customers = processor.getCustomerService().findAll();
        Map<Long, Customer> customerMap = new LinkedHashMap<>();
        customers.forEach(customer -> customerMap.put(customer.getUuid(), customer));

        return customerMap;
    }

    private Map<String,Date> getXmlReportsAsMap(List<XmlCompanyReport> filenames){

        Map<String,Date> map = new LinkedHashMap<>();
        filenames.stream().forEach(file->map.put(file.getFilename(),file.getDate()));

        return map;

    }
}
