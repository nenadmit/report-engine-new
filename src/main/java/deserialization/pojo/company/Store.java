package deserialization.pojo.company;

import deserialization.pojo.company.annotations.Column;
import deserialization.pojo.company.annotations.Table;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

@Table(name="stores")
public class Store extends Base {

    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name="fk_store_company")
    private int companyId;
    private Company company;

    private List<Receipt> receipts;
    private List<Invoice> invoices;


    public Store(int id,String name, String address) {
        this.name = name;
        this.address = address;
        super.setId(id);
    }


    public Store() {
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @XmlAttribute
    public void setAddress(String address) {
        this.address = address;
    }

    public List<Receipt> getReceipts() {
        if (receipts == null){
            receipts = new ArrayList<>();
        }

        return receipts;
    }

    @XmlElementWrapper(name = "receipts")
    @XmlElement(name="receipt")
    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @XmlElementWrapper(name = "invoices")
    @XmlElement(name="invoice")
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Invoice> getInvoices() {
        if (invoices == null){
            invoices = new ArrayList<>();
        }
        return invoices;
    }

    public enum Fields {

        ID("id"),
        NAME("name"),
        ADDRESS("address"),
        FK_STORE_COMPANY("fk_store_company");

        private final String value;

        Fields(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Store{ id :" + super.getId()+
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
               "";
    }
}
