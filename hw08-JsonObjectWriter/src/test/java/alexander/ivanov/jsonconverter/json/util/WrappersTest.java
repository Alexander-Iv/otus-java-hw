package alexander.ivanov.jsonconverter.json.util;

import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WrappersTest {
    private static final Logger logger = LoggerFactory.getLogger(WrappersTest.class);

    @Test
    void objectWrapTest() {
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
        String wrapped = new Wrappers.RecordWrapper()
                .wrap(Collections.singletonMap("PrimitiveTypes", primitiveTypeExample));
        logger.info("wrapped = " + wrapped);
        assertEquals("{ \"PrimitiveTypes\": {\"byte1\": 2, \"char1\": \"a\", \"short1\": 4, \"int1\": 8, \"long1\": 16, \"float1\": 32.2, \"double1\": 64.4, \"boolean1\": true}}", wrapped);
    }

    @Test
    void stringWrapTest() {
        String wrapped = new Wrappers.StringWrapper().wrap("wrapped string");
        assertEquals("\"wrapped string\"", wrapped);
    }

    @Test
    void digitArrayWrapTest() {
        String wrapped = new Wrappers.ArrayWrapper().wrap(Arrays.asList(1,2,3));
        assertEquals("[1,2,3]", wrapped);
    }

    @Test
    void emptyArrayWrapTest() {
        String wrapped = new Wrappers.ArrayWrapper().wrap(Arrays.asList());
        assertEquals("[]", wrapped);
    }

    @Test
    void stringArrayWrapTest() {
        String wrapped = new Wrappers.ArrayWrapper().wrap(Arrays.asList("1", "2", "3"));
        assertEquals("[\"1\",\"2\",\"3\"]", wrapped);
    }

    @Test
    void booleanWrapTest() {
        String wrapped = new Wrappers.BooleanWrapper().wrap(true);
        assertEquals("true", wrapped);
    }

    @Test
    void nullWrapTest() {
        String wrapped = new Wrappers.NullWrapper().wrap(null);
        assertEquals("null", wrapped);
    }

    @Test
    void numberValueWrapTest() {
        byte primitiveByte = (byte)2;
        String wrappedByte = new Wrappers.ValueWrapper().wrap(primitiveByte);
        assertEquals("2", wrappedByte);

        short primitiveShort = (short)4;
        String wrappedShort = new Wrappers.ValueWrapper().wrap(primitiveShort);
        assertEquals("4", wrappedShort);

        int primitiveInt = 8;
        String wrappedInt = new Wrappers.ValueWrapper().wrap(primitiveInt);
        assertEquals("8", wrappedInt);

        long primitiveLong = 16L;
        String wrappedLong = new Wrappers.ValueWrapper().wrap(primitiveLong);
        assertEquals("16", wrappedLong);

        float primitiveFloat = 32.2F;
        String wrappedFloat = new Wrappers.ValueWrapper().wrap(primitiveFloat);
        assertEquals("32.2", wrappedFloat);

        double primitiveDouble = 64.4D;
        String wrappedDouble = new Wrappers.ValueWrapper().wrap(primitiveDouble);
        assertEquals("64.4", wrappedDouble);
    }

    @Test
    void stringValueWrapTest() {
        String stringValue = "qwerty";
        String wrappedString = new Wrappers.ValueWrapper().wrap(stringValue);
        assertEquals("\"qwerty\"", wrappedString);

        char charValue = 'a';
        String wrappedChar = new Wrappers.ValueWrapper().wrap(charValue);
        assertEquals("\"a\"", wrappedChar);
    }

    @Test
    void nullValueWrapTest() {
        String wrappedNull = new Wrappers.ValueWrapper().wrap(null);
        assertEquals("null", wrappedNull);
    }

    @Test
    void arrayValueWrapTest() {
        Collection<Integer> intArrayList = new ArrayList<>();
        intArrayList.add(1);
        intArrayList.add(2);
        intArrayList.add(3);
        String wrappedIntArrayList = new Wrappers.ValueWrapper().wrap(intArrayList);
        assertEquals("[1,2,3]", wrappedIntArrayList);

        int[] primitiveIntArray = new int[]{1,2,3};
        String wrappedPrimitiveIntArray = new Wrappers.ValueWrapper().wrap(primitiveIntArray);
        assertEquals("[1,2,3]", wrappedPrimitiveIntArray);

        String wrappedArray = new Wrappers.ValueWrapper().wrap(Arrays.asList(1,2,3));
        assertEquals("[1,2,3]", wrappedArray);
    }
}