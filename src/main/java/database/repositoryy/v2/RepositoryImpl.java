package database.repositoryy.v2;

import database.builder.QueryBuilder;
import database.repositoryy.Repository;
import database.specification.QueryInfo;
import database.specification.Specification;
import database.template.Template;
import database.template.TemplateImpl;
import deserialization.pojo.company.Base;
import deserialization.pojo.company.annotations.Column;
import deserialization.pojo.company.annotations.Table;


import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepositoryImpl<T extends Base> implements Repository<T> {

    private Class<T> entityClass;
    private final String tableName;
    private Template<T> template;

    public RepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getAnnotation(Table.class).name();
        this.template = new TemplateImpl<>();
    }

    @Override
    public T create(T base) {

        Map<String, Object> params = getFieldsAsMap(entityClass.getDeclaredFields());

        String sql = new QueryBuilder().insert(tableName, params).getQuery();

        int generatedKey = template.singleStatementExecution(sql, getObjectArray(base));

        base.setId(generatedKey);

        return base;
    }

    @Override
    public List<T> createAll(List<T> list) {

        Map<String, Object> params = getFieldsAsMap(entityClass.getDeclaredFields());
        String sql = new QueryBuilder().insert(tableName, params).getQuery();

        List<Object[]> paramsObject = new ArrayList<>();

        list.stream().forEach(param -> paramsObject.add(getObjectArray(param)));

        List<Integer> generatedKeys = template.batchExecution(sql, paramsObject);

        int counter = 0;
        for (T t : list) {
            t.setId(generatedKeys.get(counter++));
        }
        return list;
    }

    /**
     * Moving all the objects in an array one step to the left, so that the id goes last
     * and the rest of object go first.
     */
    @Override
    public T update(T base) {

        Map<String, Object> params = getFieldsAsMap(entityClass.getDeclaredFields());
        String sql = new QueryBuilder()
                .update(tableName, params)
                .where().field("id", "?").getQuery();

        Object[] objectArray = getObjectArray(base);
        Object[] shiftToLeft = new Object[objectArray.length];

        shiftToLeft[shiftToLeft.length - 1] = objectArray[0];
        for (int x = 0; x < objectArray.length - 1; x++) {
            shiftToLeft[x] = objectArray[x + 1];
        }

        template.singleStatementExecution(sql, shiftToLeft);

        return base;

    }

    @Override
    public void delete(T base) {

        String sql = new QueryBuilder().delete(tableName)
                .where()
                .field("id", "?").getQuery();

        template.singleStatementExecution(sql, new Object[]{base.getId()});
    }

    @Override
    public List<T> query(Specification specification) {

        QueryInfo queryInfo = specification.toQueryInfo();

        return template.findAll(queryInfo.getSql(), queryInfo.getParams(), this::map);
    }

    private T map(ResultSet resultSet) throws SQLException {

        T base = null;

        try {
            base = entityClass.getDeclaredConstructor().newInstance();
            base.setId(resultSet.getInt("id"));

            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {

                    String columnName = field.getAnnotation(Column.class).name();
                    field.setAccessible(true);
                    field = setField(field, resultSet, base, columnName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base;
    }

    private Object[] getObjectArray(T base) {

        List<Object> objects = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {

            if (field.isAnnotationPresent(Column.class)) {

                try {
                    field.setAccessible(true);
                    objects.add(field.get(base));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return objects.toArray();
    }

    private Map<String, Object> getFieldsAsMap(Field[] fields) {

        Map<String, Object> params = new LinkedHashMap<>();

        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> params.put(field.getAnnotation(Column.class).name(), "?"));

        return params;
    }

    /**
     * Using getObject when the column in Database is of type TinyInt will return an int in range of (0-1)
     * which causes problems when the field in class is of type Boolean.
     */

    private Field setField(Field field, ResultSet resultSet,
                           T base, String columnName) throws SQLException, IllegalAccessException {

        if (field.getType() == Boolean.class) {
            field.set(base, resultSet.getBoolean(columnName));
        } else {
            field.set(base, resultSet.getObject(columnName));
        }

        return field;


    }
}
