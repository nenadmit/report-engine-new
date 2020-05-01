package database.specification;

import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import deserialization.pojo.company.annotations.Table;

public class GetBillsByCompanyAndMonth<T> implements Specification {

    private int companyId;
    private int month;
    private int year;
    private String paymentType = "";
    private final String tableName;

    public GetBillsByCompanyAndMonth(Class<T> billClass, int companyId, int month, int year) {
        this.companyId = companyId;
        this.month = month;
        this.year = year;
        this.tableName = billClass.getAnnotation(Table.class).name();
    }

    public GetBillsByCompanyAndMonth(Class<T> billClass, int companyId, int month, int year, String paymentType) {
        this.companyId = companyId;
        this.month = month;
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
                "where month(i.date_time) = ? " +
                "and year(i.date_time) = ? " + filterByPaymentType +
                "and c.id =? " +
                "group by i.id ";

        if(!paymentType.isEmpty()) {
            return new QueryInfo(sql,new Object[]{month,year,paymentType,companyId});
        }
        return new QueryInfo(sql,new Object[]{month,year,companyId});
    }
}

