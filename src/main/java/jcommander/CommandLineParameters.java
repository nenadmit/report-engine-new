package jcommander;

import com.beust.jcommander.Parameter;

import java.util.List;

public class CommandLineParameters {


    @Parameter(names="config",
    description = "Used for configuring the import and export directories")
    private boolean config = false;

    @Parameter(names = {"--data-dir", "-dd"},
            description = "Path to directory for saving .xml files",
            validateWith = DirectoryParameterValidator.class)
    private String importDirectory;

    @Parameter(names = {"--export-dir", "-ed"},
            description = "Path to a directory for exporting .xlsx files",
            validateWith = DirectoryParameterValidator.class)
    private String exportDirectory;

    @Parameter(names={"download","-dl"},
    description = "Downloads data from sftp server")
    private boolean download = false;

    @Parameter(names={"process", "-p"},
    description = "Processes the data from importDirectory into a database")
    private boolean process = false;

    @Parameter(names={"--help","-h"},
    description = "Shows available commands",
    help = true)
    private boolean help = false;

    @Parameter(names="report",
    description = "Exports data as xlsx file")
    private boolean report = false;

    @Parameter(names={"--company","-c"})
    private String companyName;

    @Parameter(names={"--month","-m"})
    private List<Integer> months;

    @Parameter(names={"--year","-y"})
    public int year;

    @Parameter(names={"--quarter","-q"})
    private String quarter;

    @Parameter(names={"--aggregation","-a"})
    private String aggregation;

    @Parameter(names="-top")
    private int top;

    @Parameter(names="-order")
    private String order;


    public boolean isConfig() {
        return config;
    }

    public void setConfig(boolean config) {
        this.config = config;
    }

    public String getImportDirectory() {
        return importDirectory;
    }

    public void setImportDirectory(String importDirectory) {
        this.importDirectory = importDirectory;
    }

    public String getExportDirectory() {
        return exportDirectory;
    }

    public void setExportDirectory(String exportDirectory) {
        this.exportDirectory = exportDirectory;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Integer> getMonths() {
        return months;
    }

    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }
}
