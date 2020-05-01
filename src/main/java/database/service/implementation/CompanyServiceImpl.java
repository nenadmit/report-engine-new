package database.service.implementation;

import database.repositoryy.Repository;
import database.service.interfaces.CompanyService;
import database.specification.FindBySingleFieldSpec;
import deserialization.pojo.company.Card;
import deserialization.pojo.company.Company;
import deserialization.pojo.company.annotations.Table;

import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl extends GenericService<Company> implements CompanyService {

    private Repository<Company> repository;
    private String tableName;

    public CompanyServiceImpl(Repository repository) {

        super(repository, Company.class);
        this.repository = repository;
        this.tableName = Company.class.getAnnotation(Table.class).name();
    }


    @Override
    public Optional<Company> findByName(String name) {

        return checkIfExists(
                repository.query(new FindBySingleFieldSpec<String>(name,
                        tableName,
                        Company.Fields.NAME.getValue()))
        );
    }

    @Override
    public Optional<Company> findByUuid(long uuid) {
        return checkIfExists(
                repository.query(new FindBySingleFieldSpec<Long>(uuid,
                        tableName,
                        Company.Fields.UUID.getValue()))
        );
    }

    @Override
    public Company save(Company company) {

        Optional<Company> optional = findByUuid(company.getUuid());

        if(optional.isPresent()){
            company.setId(optional.get().getId());
            return company;
        }
        return super.save(company);
    }

    private Optional<Company> checkIfExists(List<Company> list) {

        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list.get(0));
    }
}
