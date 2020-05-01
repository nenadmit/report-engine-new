package deserialization.pojo.company;

import deserialization.pojo.company.annotations.Column;
import deserialization.pojo.company.annotations.Table;

import java.util.Date;

@Table(name="parsed_xml_files")
public class XmlCompanyReport extends Base{

    @Column(name="filename")
    private String filename;
    @Column(name="date_time")
    private Date date;

    public XmlCompanyReport() {

    }

    public XmlCompanyReport(String filename, Date date) {
        this.filename = filename;
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
