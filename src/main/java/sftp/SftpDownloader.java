package sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import database.CompanyDataProcessor;
import database.service.implementation.XmlCompanyFileService;
import deserialization.pojo.company.XmlCompanyReport;

import java.io.File;
import java.util.*;

public class SftpDownloader {

    private SftpDownloaderConfigurer configurer;
    private CompanyDataProcessor processor;
    private ChannelSftp channelSftp;

    public SftpDownloader(SftpDownloaderConfigurer configurer, CompanyDataProcessor processor) {
        this.configurer = configurer;
        this.processor = processor;
    }

    /**
     * Instantiates the session object using SftpConfigurer and creates an instance
     * of ChannelSftp.
     * Downloads files and saves them to @param saveFilePath
     *
     * @return List<File> of saved files.
     */

    public void downloadFiles(String saveFilePath) {

        Map<String, Date> parsedXmlFiles = getParsedFilesAsMap();
        int numberOfNewFiles = 0;

        Session session = configurer.getSession();

        try {

            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(configurer.getRootDirectory());

            List<ChannelSftp.LsEntry> list = channelSftp.ls("*");
            int counter = 0;

            for (ChannelSftp.LsEntry file : list) {

                if (!parsedXmlFiles.containsKey(file.getFilename())) {

                    System.out.println("Downloading file: " + file.getFilename() + " " + counter++ + "/" + list.size());
                    channelSftp.get(file.getFilename(), saveFilePath);
                    numberOfNewFiles++;
                }
            }


        } catch (SftpException | JSchException e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channelSftp.disconnect();
        }

        if (numberOfNewFiles > 0) {
            System.out.println("Downloaded " + numberOfNewFiles + "new files!");
        } else {
            System.out.println("No new files found!");
        }

    }

    private Map<String, Date> getParsedFilesAsMap() {

        Map<String, Date> parsedXmlFiles = new HashMap<>();
        List<XmlCompanyReport> xmlFiles = processor.getXmlCompanyFileService().findAll();

        xmlFiles.stream().forEach(file -> parsedXmlFiles.put(file.getFilename(), file.getDate()));

        return parsedXmlFiles;
    }

}



