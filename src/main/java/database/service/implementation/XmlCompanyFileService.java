package database.service.implementation;

import database.repositoryy.Repository;
import deserialization.pojo.company.XmlCompanyReport;

public class XmlCompanyFileService extends GenericService<XmlCompanyReport> {

    public XmlCompanyFileService(Repository<XmlCompanyReport> repository) {
        super(repository, XmlCompanyReport.class);
    }
}
