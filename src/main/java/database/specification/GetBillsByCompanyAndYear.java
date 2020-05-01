package database.specification;

import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import deserialization.pojo.company.annotations.Table;

public class GetBillsByCompanyAndYear<T> implements Specification {

    private int companyId;
    private int year;
    private final String tableName;
    private String paymentType = "";

    public GetBillsByCompanyAndYear(Class<T> billClass, int companyId, int year) {
        this.companyId = companyId;
        this.year = year;
        this.tableName = billClass.getAnnotation(Table.class).name();
    }
    public GetBillsByCompanyAndYear(Class<T> billClass, int companyId, int year,String paymentType) {
        this.companyId = companyId;
        this.year = year;
        this.tableName = billClass.getAnnotation(Table.class).name();
        this.paymentType = paymentType;
    }

    @Override
    public QueryInfo toQueryInfo() {

        String foreignKey = tableName.equals("invoices") ? Invoice.Fields.FK_INVOICE_STORE.getValue()
                : Receipt.Fields.FK_RECEIPT_STORE.getValue();

        String filterByPaymentType = " and i.payment_type = ? ";

        String sql = "select i.* from "+tableName+" i " +
                "join stores s on s.id = i."+foreignKey+" " +
                "join companies c on c.id = s.fk_store_company " +
                "and year(i.date_time) = ? " + filterByPaymentType +
                "and c.id =? " +
                "group by i.id ";

        if(!paymentType.isEmpty()){
            return new QueryInfo(sql, new Object[]{year,paymentType,companyId});
        }

        return new QueryInfo(sql,new Object[]{year,companyId});
    }
}

