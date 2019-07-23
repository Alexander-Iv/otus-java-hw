package alexander.ivanov.jsonconverter;

import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import alexander.ivanov.jsonconverter.impl.JsonConverterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        PrimitiveTypeExample primitiveTypeExample = new PrimitiveTypeExample(
                (byte)2,
                'a',
                (short)4,
                8,
                16L,
                32.2F,
                64.4D,
                true
        );

        logger.info("primitiveTypeExample = \n{}", primitiveTypeExample);
        JsonConverter converter = new JsonConverterImpl();
        String convertedPrimitiveTypeExample = converter.toJson(primitiveTypeExample);
        logger.info("convertedPrimitiveTypeExample = \n{}", convertedPrimitiveTypeExample);
    }
}
