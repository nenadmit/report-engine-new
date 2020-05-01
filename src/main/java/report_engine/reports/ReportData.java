package report_engine.reports;

import deserialization.pojo.company.Invoice;
import deserialization.pojo.company.Receipt;
import org.exolab.castor.types.DateTime;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportData implements Comparable<ReportData> {


    String reportType;
    String aggregationName;
    private Map<String, Double> turnoverPerTimePeriod;
    private double total;
    private List<Invoice> invoices;
    private List<Receipt> receipts;

    public ReportData(String reportType, String aggregationName, List<Invoice> invoices, List<Receipt> receipts) {
        this.reportType = reportType;
        this.aggregationName = aggregationName;
        this.invoices = invoices;
        this.receipts = receipts;
        turnoverPerTimePeriod = setupReport();
        total = calculateTotal();
    }

    private Map<String,Double> setupReport() {
        Map<String, Double> dataMap = new LinkedHashMap<>();

        for (Invoice invoice : invoices) {
            String quarter = getTimePeriod(invoice.getDatetime());
            if (!dataMap.containsKey(quarter)) {
                dataMap.put(quarter, invoice.getTotal());
            } else {
                dataMap.put(quarter, dataMap.get(quarter) + invoice.getTotal());
            }
        }
        for (Receipt receipt : receipts) {
            String quarter = getTimePeriod(receipt.getDatetime());
            if (!dataMap.containsKey(quarter)) {
                dataMap.put(quarter, receipt.getTotal());
            } else {
                dataMap.put(quarter, dataMap.get(quarter) + receipt.getTotal());
            }
        }
        return dataMap;
    }

    private double calculateTotal() {

        double turnover = 0;
        for (Double total : turnoverPerTimePeriod.values()) {
            turnover  = turnover + total;
        }
        return turnover;
    }

    private String getTimePeriod(Date date) {

        if (reportType.equals(ReportConstants.MONTHLY_REPORT)) {
//            return String.valueOf(new DateTime(date).getDay());
            return new SimpleDateFormat("dd-MM-yyyy").format(date);
        } else if (reportType.equals(ReportConstants.QUARTERLY_REPORT)) {
            return Month.of(new DateTime(date).getMonth()).name();
        } else if (reportType.equals(ReportConstants.YEARLY_REPORT)) {
            return getQuarter(new DateTime(date).getMonth());
        } else {
            throw new IllegalArgumentException("Unknown report type" + reportType);
        }
    }

    private String getQuarter(int month) {

        if (month > 0 && month <= 3) {
            return "QUARTER 1";
        } else if (month > 3 && month <= 6) {
            return "QUARTER 2";
        } else if (month > 6 && month <= 9) {
            return "QUARTER 3";
        } else if (month > 9 && month <= 12) {
            return "QUARTER 4";
        } else {
            throw new IllegalArgumentException("month cannot be less than 0 or greather than 12");
        }
    }

    public String getAggregationName() {
        return aggregationName;
    }

    protected void setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
    }

    public Map<String, Double> getTurnoverPerTimePeriod() {
        return turnoverPerTimePeriod;
    }

    protected void setTurnoverPerTimePeriod(Map<String, Double> turnoverPerTimePeriod) {
        this.turnoverPerTimePeriod = turnoverPerTimePeriod;
    }

    public double getTotal() {
        return total;
    }

    protected void setTotal(double total) {

        this.total = total;
    }

    @Override
    public int compareTo(ReportData o) {
        return Double.compare(getTotal(),o.getTotal());
    }
}
