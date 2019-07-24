package alexander.ivanov.jsonconverter.impl;

import alexander.ivanov.jsonconverter.JsonConverter;
import alexander.ivanov.jsonconverter.json.JsonWrappers;
import alexander.ivanov.jsonconverter.json.util.FieldUtil;

public class JsonConverterImpl implements JsonConverter {
    @Override
    public String toJson(Object object) {
        return new JsonWrappers.RecordWrapper().wrap(FieldUtil.getFieldsNameValueMap(object));
    }
}
