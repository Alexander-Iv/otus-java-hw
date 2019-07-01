package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.Dao;
import alexander.ivanov.orm.data.source.h2.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateImpl.class);
    private String simpleSelect;
    private String create;
    private String insert;
    private String update;
    private String select;
    private Dao dao;

    public JdbcTemplateImpl(Dao dao) {
        this.dao = dao;
    }

    @Override
    public void create(T objectData) {
        dao.create(getCreate(objectData));
        dao.insert(getInsert(objectData), ReflectionHelper.getFieldValues(objectData));
    }

    @Override
    public void update(T objectData) {
        dao.update(getUpdate(objectData), ReflectionHelper.getFieldValues(objectData));
    }

    @Override
    public void createOrUpdate(T objectData) {
        if (dao.select(getSimpleSelect(objectData), ReflectionHelper.getIdValue(objectData)) == 0) {
            create(objectData);
        } else {
            update(objectData);
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return dao.getInstance(id, clazz);
    }

    public String getSimpleSelect(T objectData) {
        return simpleSelect == null || simpleSelect.isEmpty()
                ? simpleSelect = ReflectionHelper.objectToSimpleSelect(objectData)
                : simpleSelect;
    }

    private String getCreate(T objectData) {
        return create == null || create.isEmpty()
                ? create = ReflectionHelper.objectToCreate(objectData)
                : create;
    }

    private String getInsert(T objectData) {
        return insert == null || insert.isEmpty()
                ? insert = ReflectionHelper.objectToInsert(objectData)
                : insert;
    }

    private String getUpdate(T objectData) {
        return update == null || update.isEmpty()
                ? update = ReflectionHelper.objectToUpdate(objectData)
                : update;
    }

    private String getSelect(T objectData) {
        return select == null || select.isEmpty()
                ? select = ReflectionHelper.objectToSelect(objectData)
                : select;
    }
}
