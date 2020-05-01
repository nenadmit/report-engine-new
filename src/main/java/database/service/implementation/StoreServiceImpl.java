package database.service.implementation;

import database.repositoryy.Repository;
import database.service.interfaces.StoreService;
import database.specification.FindBySingleFieldSpec;
import deserialization.pojo.company.Store;
import deserialization.pojo.company.annotations.Table;

import java.util.List;
import java.util.Optional;

public class StoreServiceImpl extends GenericService<Store> implements StoreService {

    private Repository<Store> repository;
    private String storeTable;

    public StoreServiceImpl(Repository<Store> repository) {
        super(repository, Store.class);
        this.repository = repository;
        this.storeTable = Store.class.getAnnotation(Table.class).name();
    }

    @Override
    public List<Store> findByCompany(int companyId) {
        return repository.query(
                new FindBySingleFieldSpec<>(companyId,
                        storeTable,
                        Store.Fields.FK_STORE_COMPANY.getValue()));
    }

    @Override
    public Optional<Store> findByName(String name) {

        List<Store> list =  repository.query(new FindBySingleFieldSpec<>(name,
                storeTable,
                Store.Fields.NAME.getValue()));

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }


    @Override
    public Store save(Store store){

       Optional<Store> optional = findByName(store.getName());

        if(optional.isPresent()){
            store.setId(optional.get().getId());
            return store;
        }
        return super.save(store);
    }
}
