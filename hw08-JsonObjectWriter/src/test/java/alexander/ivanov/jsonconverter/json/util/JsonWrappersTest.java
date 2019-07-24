package alexander.ivanov.jsonconverter.json.util;

import alexander.ivanov.jsonconverter.data.ArraysPrimitiveTypeExample;
import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import alexander.ivanov.jsonconverter.json.JsonWrappers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWrappersTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonWrappersTest.class);

    @Test
    void objectWrapTest() {
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

        PrimitiveTypeExample primitiveDefault = PrimitiveTypeExample.builder().build();

        Map<String, Object> records = new LinkedHashMap<>();
        records.put("record1", primitiveTypeExample);
        records.put("record2", primitiveDefault);


        String wrapped = new JsonWrappers.RecordWrapper().wrap(records);
        logger.info("wrapped = " + wrapped);
        assertEquals("{ \"record1\": {\"byte1\": 2, \"char1\": \"a\", \"short1\": 4, \"int1\": 8, \"long1\": 16, \"float1\": 32.2, \"double1\": 64.4, \"boolean1\": true}, \"record2\": {\"byte1\": 0, \"char1\": \"\\u0000\", \"short1\": 0, \"int1\": 0, \"long1\": 0, \"float1\": 0.0, \"double1\": 0.0, \"boolean1\": false}}", wrapped);
    }

    @Test
    void objectArrayWrapTest() {
        ArraysPrimitiveTypeExample arraysPrimitiveTypeExample1 = ArraysPrimitiveTypeExample.builder()
                .bytePrimitiveArray(new byte[]{})
                .charPrimitiveArray(new char[]{})
                .shortPrimitiveArray(new short[]{})
                .intPrimitiveArray(new int[]{})
                .longPrimitiveArray(new long[]{})
                .floatPrimitiveArray(new float[]{})
                .doublePrimitiveArray(new double[]{})
                .booleanPrimitiveArray(new boolean[]{})
                .build();

        Map<String, Object> records = new LinkedHashMap<>();
        records.put("emptyArray", arraysPrimitiveTypeExample1);

        String wrapped = new JsonWrappers.RecordWrapper().wrap(records);
        logger.info("wrapped = " + wrapped);
        assertEquals("{ \"emptyArray\": {\"bytePrimitiveArray\": [], \"charPrimitiveArray\": [], \"shortPrimitiveArray\": [], \"intPrimitiveArray\": [], \"longPrimitiveArray\": [], \"floatPrimitiveArray\": [], \"doublePrimitiveArray\": [], \"booleanPrimitiveArray\": []}}", wrapped);

        Map<String, Object> records2 = new LinkedHashMap<>();
        ArraysPrimitiveTypeExample arraysPrimitiveTypeExample2 = ArraysPrimitiveTypeExample.builder()
                .bytePrimitiveArray(new byte[]{1,11,111})
                .charPrimitiveArray(new char[]{'a','b','c'})
                .shortPrimitiveArray(new short[]{2,22,222})
                .intPrimitiveArray(new int[]{3,33,333})
                .longPrimitiveArray(new long[]{4L,44L,444L})
                .floatPrimitiveArray(new float[]{5.5F,5.55F,5.555F})
                .doublePrimitiveArray(new double[]{6.6D,6.66D,6.666D})
                .booleanPrimitiveArray(new boolean[]{true, false, true})
                .build();

        records2.put("emptyArray", arraysPrimitiveTypeExample2);

        String wrapped2 = new JsonWrappers.RecordWrapper().wrap(records2);
        logger.info("wrapped = " + wrapped2);
        assertEquals("{ \"emptyArray\": {\"bytePrimitiveArray\": [1,11,111], \"charPrimitiveArray\": [\"a\",\"b\",\"c\"], \"shortPrimitiveArray\": [2,22,222], \"intPrimitiveArray\": [3,33,333], \"longPrimitiveArray\": [4,44,444], \"floatPrimitiveArray\": [5.5,5.55,5.555], \"doublePrimitiveArray\": [6.6,6.66,6.666], \"booleanPrimitiveArray\": [true,false,true]}}", wrapped2);
    }

    @Test
    void stringWrapTest() {
        String wrapped = new JsonWrappers.StringWrapper().wrap("wrapped string");
        assertEquals("\"wrapped string\"", wrapped);
    }

    @Test
    void digitArrayWrapTest() {
        String wrapped = new JsonWrappers.ArrayWrapper().wrap(Arrays.asList(1,2,3));
        assertEquals("[1,2,3]", wrapped);
    }

    @Test
    void emptyArrayWrapTest() {
        String wrapped = new JsonWrappers.ArrayWrapper().wrap(Arrays.asList());
        assertEquals("[]", wrapped);
    }

    @Test
    void stringArrayWrapTest() {
        String wrapped = new JsonWrappers.ArrayWrapper().wrap(Arrays.asList("1", "2", "3"));
        assertEquals("[\"1\",\"2\",\"3\"]", wrapped);
    }

    @Test
    void booleanWrapTest() {
        String wrapped = new JsonWrappers.BooleanWrapper().wrap(true);
        assertEquals("true", wrapped);
    }

    @Test
    void nullWrapTest() {
        String wrapped = new JsonWrappers.NullWrapper().wrap(null);
        assertEquals("null", wrapped);
    }

    @Test
    void numberValueWrapTest() {
        byte primitiveByte = (byte)2;
        String wrappedByte = new JsonWrappers.ValueWrapper().wrap(primitiveByte);
        assertEquals("2", wrappedByte);

        short primitiveShort = (short)4;
        String wrappedShort = new JsonWrappers.ValueWrapper().wrap(primitiveShort);
        assertEquals("4", wrappedShort);

        int primitiveInt = 8;
        String wrappedInt = new JsonWrappers.ValueWrapper().wrap(primitiveInt);
        assertEquals("8", wrappedInt);

        long primitiveLong = 16L;
        String wrappedLong = new JsonWrappers.ValueWrapper().wrap(primitiveLong);
        assertEquals("16", wrappedLong);

        float primitiveFloat = 32.2F;
        String wrappedFloat = new JsonWrappers.ValueWrapper().wrap(primitiveFloat);
        assertEquals("32.2", wrappedFloat);

        double primitiveDouble = 64.4D;
        String wrappedDouble = new JsonWrappers.ValueWrapper().wrap(primitiveDouble);
        assertEquals("64.4", wrappedDouble);
    }

    @Test
    void stringValueWrapTest() {
        String stringValue = "qwerty";
        String wrappedString = new JsonWrappers.ValueWrapper().wrap(stringValue);
        assertEquals("\"qwerty\"", wrappedString);

        char charValue = 'a';
        String wrappedChar = new JsonWrappers.ValueWrapper().wrap(charValue);
        assertEquals("\"a\"", wrappedChar);
    }

    @Test
    void nullValueWrapTest() {
        String wrappedNull = new JsonWrappers.ValueWrapper().wrap(null);
        assertEquals("null", wrappedNull);
    }

    @Test
    void arrayValueWrapTest() {
        Collection<Integer> intArrayList = new ArrayList<>();
        intArrayList.add(1);
        intArrayList.add(2);
        intArrayList.add(3);
        String wrappedIntArrayList = new JsonWrappers.ValueWrapper().wrap(intArrayList);
        assertEquals("[1,2,3]", wrappedIntArrayList);

        int[] primitiveIntArray = new int[]{1,2,3};
        String wrappedPrimitiveIntArray = new JsonWrappers.ValueWrapper().wrap(primitiveIntArray);
        assertEquals("[1,2,3]", wrappedPrimitiveIntArray);

        String wrappedArray = new JsonWrappers.ValueWrapper().wrap(Arrays.asList(1,2,3));
        assertEquals("[1,2,3]", wrappedArray);
    }
}