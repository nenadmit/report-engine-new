import database.CompanyDataParser;
import database.CompanyDataProcessor;
import database.FlywayConfiguration;
import deserialization.XmlParser;
import deserialization.pojo.company.Company;
import sftp.SftpDownloader;
import sftp.SftpDownloaderConfigurer;

import java.io.File;
import java.text.ParseException;
import java.util.List;

public class TestMain {



    public static void main(String[] args) throws ParseException {

        FlywayConfiguration.getFlyway().migrate();

        Main main = new Main();

        new SftpDownloader(new SftpDownloaderConfigurer(),main.dataProcessor()).downloadFiles("data");
        CompanyDataParser dataParser = new CompanyDataParser(main.dataProcessor(),new XmlParser<>(Company.class));

        File directory = new File("data");

        long start = System.nanoTime();
        List<Company> companies = dataParser.parseAll(directory.listFiles());
        dataParser.saveAll(companies);
        long end = System.nanoTime();

        System.out.println("Runtime: "+ ((end-start)/1000000000));

    }


}
