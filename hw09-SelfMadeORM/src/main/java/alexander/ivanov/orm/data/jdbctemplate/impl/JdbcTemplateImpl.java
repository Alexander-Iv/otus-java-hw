package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.H2;
import alexander.ivanov.orm.data.source.h2.impl.H2Impl;
import alexander.ivanov.orm.data.source.h2.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplateImpl implements JdbcTemplate<Object> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateImpl.class);

    private H2 db = new H2Impl("jdbc:h2:mem:default");

    @Override
    public void create(Object objectData) {
        int count = 0;
        db.create(ReflectionHelper.objectToCreate(objectData));

        //logger.info("**************************before insert");
        db.insert(ReflectionHelper.objectToInsert(objectData));
        /*logger.info("***************************after insert");
        db.select(ReflectionHelper.objectToSelect(objectData));
        logger.info("***************************************");*/
    }

    @Override
    public void update(Object objectData) {
        //logger.info("******************************** update");
        db.update(ReflectionHelper.objectToUpdate(objectData));
        /*logger.info("***************************after update");
        db.select(ReflectionHelper.objectToSelect(objectData));*/
    }

    @Override
    public void createOrUpdate(Object objectData) {
        if (db.select(ReflectionHelper.objectToSelect(objectData)).size() == 0) {
            create(objectData);
        } else {
            update(objectData);
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return (T) ReflectionHelper.classToObject(id, clazz, db);
    }
}
