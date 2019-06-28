package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.DataDefinitionAndManipulation;
import alexander.ivanov.orm.data.source.h2.util.ObjectCreaterFromClassById;
import alexander.ivanov.orm.data.source.h2.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateImpl.class);
    private String create;
    private String insert;
    private String update;
    private String select;
    private List fieldValues;
    private List idValue;
    private DataDefinitionAndManipulation ddm;

    public JdbcTemplateImpl(DataDefinitionAndManipulation ddm) {
        this.ddm = ddm;
    }

    @Override
    public void create(T objectData) {
        ddm.create(getCreate(objectData));

        //logger.info("**************************before insert");
        ddm.insert(getInsert(objectData), getFieldValues(objectData));
        /*logger.info("***************************after insert");
        ddm.select(ReflectionHelper.objectToSelect(objectData));
        logger.info("***************************************");*/
    }

    @Override
    public void update(T objectData) {
        //logger.info("******************************** update");
        ddm.update(getUpdate(objectData), getFieldValues(objectData));
        logger.info("***************************after update");
        ddm.select(getSelect(objectData), getIdValue(objectData));
    }

    @Override
    public void createOrUpdate(T objectData) {
        if (ddm.select(getSelect(objectData), getIdValue(objectData)).size() == 0) {
            create(objectData);
        } else {
            update(objectData);
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        Object object = new ObjectCreaterFromClassById(id, clazz, ddm).getInstance();
        logger.info("object = " + object);
        return (T) object;
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

    private List getFieldValues(T objectData) {
        return fieldValues == null || fieldValues.isEmpty()
                ? fieldValues = ReflectionHelper.getFieldValues(objectData)
                : fieldValues;
    }

    private List getIdValue(T objectData) {
        return idValue == null || idValue.isEmpty()
                ? idValue = ReflectionHelper.getIdValue(objectData)
                : idValue;
    }
}
