package jcommander;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import configuration.AppConfiguration;
import configuration.AppConfigurer;
import configuration.DataDirectories;
import database.CompanyDataParser;
import database.CompanyDataProcessor;
import database.FlywayConfiguration;
import deserialization.XmlParser;
import deserialization.pojo.company.Company;
import org.flywaydb.core.Flyway;
import sftp.SftpDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandLineParser {

    private AppConfiguration configuration;
    private DataDirectories directories;
    private SftpDownloader downloader;
    private CompanyDataProcessor dataProcessor;
    private CompanyDataParser dataParser;
    private CommandLineParameters parameters;
    private Company company;
    private XmlParser<Company> xmlParser;

    public CommandLineParser(SftpDownloader downloader,
                             CompanyDataProcessor dataProcessor,
                             CompanyDataParser parser,
                             XmlParser xmlParser) {
        this.downloader = downloader;
        this.dataProcessor = dataProcessor;
        this.dataParser = parser;
        this.xmlParser = xmlParser;

        this.configuration = AppConfigurer.getConfiguration();
        this.directories = configuration.getDataDirectories();
    }


    public void parse(String[] args) {

        JCommander commander = new JCommander(parameters);
        commander.setProgramName("hyper-reports");

        commander.parse(args);

        if (parameters.isHelp()) {
            commander.usage();
            return;
        }

        if (parameters.isConfig()) {
            if (parameters.getImportDirectory() != null)
                setImportDirectory(parameters.getImportDirectory());

            if (parameters.getExportDirectory() != null)
                setExportDirectory(parameters.getExportDirectory());
        }


        if (parameters.isDownload()) {
            downloadDataFromFtpServer();
        }

        if (parameters.isProcess()) {
            processFiles();
        }

        if(parameters.isReport()){
            validateReportParameters();
//            exportParsedData();
        }

    }

    public void setImportDirectory(String dataDirectory) {
        directories.setImportDir(dataDirectory);

        XmlParser parser = new XmlParser(AppConfiguration.class);
        parser.marshall(configuration, AppConfigurer.getConfigFile());
    }

    public void setExportDirectory(String exportDirectory) {
        directories.setExportDir(exportDirectory);

        XmlParser parser = new XmlParser(AppConfiguration.class);
        parser.marshall(configuration, AppConfigurer.getConfigFile());

    }

    public void downloadDataFromFtpServer() {

        String importDir = configuration.getDataDirectories().getImportDir();
        if (importDir.isEmpty() || importDir == null) {
            throw new ParameterException("Import data directory not set! Use command --data-dir | -dd <directory path>");
        }
        downloader.downloadFiles(importDir);
    }

    public void processFiles() {

        String importDir = configuration.getDataDirectories().getImportDir();

        if (importDir.isEmpty()) {
            throw new ParameterException("Import data directory not set! Use command --data-dir | -dd <directory path>");
        }

        Flyway flyway = FlywayConfiguration.getFlyway();

        flyway.validate();

        File importDirectory = new File(importDir);
        List<Company> companies = new ArrayList<>();
        Arrays.stream(importDirectory.listFiles())
                .forEach(file -> companies.add(xmlParser.parse(file)));

        System.out.println("Saving files to the databases.");
        long start = System.nanoTime();
        dataParser.saveAll(companies);
        long end = System.nanoTime();
        System.out.println("Completed! Runtime: " + ((end-start)/1000000000) + " seconds");

    }

    public void validateReportParameters(){

        if(directories.getExportDir().isEmpty()){
            throw new ParameterException("Export directory cannot be empty, use command --export-dir | " +
                    " -ed <directory path> to set export directory for saving .xlsx reports.");
        }

        String companyName = parameters.getCompanyName();
        Optional<Company> optionalCompany = dataProcessor.getCompanyService().findByName(companyName);

        if(!optionalCompany.isPresent()){
            throw new ParameterException(String.format("Company %s, not found",companyName));
        }else{
            this.company = optionalCompany.get();
        }

        if(parameters.getYear() == 0){
            throw new ParameterException("Year value value in hyper-reports report --year | -y can't be left blank");
        }

        if(parameters.getAggregation() == null){
            throw new ParameterException("Aggregation value in the <report> command cannot be blank, usage " +
                    "hyper-reports report -company <Name> -year <year> -month | -quarter -aggregation");
        }else{
            validateAggregationParameter(parameters.getAggregation());
        }

        if(parameters.getQuarter() != null){
            validateQuarterParameter(parameters.getQuarter());
        }
    }


//    public void exportParsedData(){
//
//        ReportEngine engine = new ReportEngine(
//                dataProcessor.getStoreService(),
//                dataProcessor.getInvoiceService(),
//                dataProcessor.getReceiptService()
//        );
//
//        XlsxParser parser = new XlsxParser();
//        Report report = new Report();
//
//        //@todo remove this copying arrays
//        int[] months = new int[parameters.getMonths().size()];
//        for(int i =0;i<months.length;i++)
//            months[i] = parameters.getMonths().get(i);
//
//        switch (parameters.getAggregation()){
//            case "store":{
//                report = engine.createStoreReport(months,parameters.getYear(),company,true,0);
//                break;
//            }
//            case "invoice":{
//                report = engine.createInvoiceReport(months,parameters.getYear(),company);
//                break;
//            }
//            case "receipt":{
//                report = engine.createReceiptReport(months,parameters.getYear(),company);
//                break;
//            }
//            case "payment":{
//                report = engine.createPaymentTypeReport(months,parameters.getYear(),company);
//                break;
//            }
//            default:{
//                throw new ParameterException(String.format("Aggergation of type %s unknown," +
//                        " acceptable aggregation parameters <store, invoice, receipt, payment>",parameters.getAggregation()));
//            }
//        }
//
//        try {
//            parser.exportAsXlsx(report,"testfile");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    public void validateQuarterParameter(String quarter){

        quarter = quarter.toLowerCase();

        switch (quarter){
            case "q1": parameters.setMonths(Arrays.asList(1,2,3));break;
            case "q2": parameters.setMonths(Arrays.asList(4,5,6));break;
            case "q3": parameters.setMonths(Arrays.asList(7,8,9));break;
            case "q4": parameters.setMonths(Arrays.asList(10,11,12));break;
            default:parameters.setMonths(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
        }
    }

    public void validateMonthParameter(int month){
        if (month <0 || month > 12){
            throw new ParameterException("Month parameter cannot be less than 0 or greather that 12");
        }
    }

    public String validateAggregationParameter(String aggregation){

        aggregation = aggregation.toLowerCase();

        List<String> aggregationParameters = Arrays.asList("store","invoice","receipt","payment");

        if(!aggregationParameters.contains(aggregation)){
            throw new ParameterException(String.format("Unknown value %s for aggregation parameter." +
                    " Aggregation parameters -> (store, receipt, invoice, payment)", aggregation));
        }

        return aggregation;

    }

    public void setParameters(CommandLineParameters parameters) {
        this.parameters = parameters;
    }
}
