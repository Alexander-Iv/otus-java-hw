package alexander.ivanov.jsonconverter;

import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import alexander.ivanov.jsonconverter.impl.JsonConverterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        PrimitiveTypeExample primitiveTypeExample = PrimitiveTypeExample.builder()
                .byte1((byte)2)
                .char1('a')
                .short1((short)4)
                .int1(8)
                .long1(16L)
                .float1(32.2F)
                .double1(64.4D)
                .boolean1(true)
                .build();

        logger.info("primitiveTypeExample = \n{}", primitiveTypeExample);
        JsonConverter converter = new JsonConverterImpl();
        String convertedPrimitiveTypeExample = converter.toJson(primitiveTypeExample);
        logger.info("convertedPrimitiveTypeExample = \n{}", convertedPrimitiveTypeExample);
    }
}
