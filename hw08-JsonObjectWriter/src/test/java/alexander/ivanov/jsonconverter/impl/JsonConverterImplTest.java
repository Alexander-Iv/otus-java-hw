package alexander.ivanov.jsonconverter.impl;

import alexander.ivanov.jsonconverter.JsonConverter;
import alexander.ivanov.jsonconverter.data.ArraysPrimitiveTypeExample;
import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import com.google.gson.Gson;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonConverterImplTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonConverterImplTest.class);

    @Test
    void defaultPrimitivesToJson() {
        checkEqualsJsonObject(PrimitiveTypeExample.builder().build(), PrimitiveTypeExample.builder().build(), PrimitiveTypeExample.class);
    }

    @Test
    void arraysToJson() {
        ArraysPrimitiveTypeExample arraysPrimitiveTypeExample = ArraysPrimitiveTypeExample.builder()
                .bytePrimitiveArray(new byte[]{1,11,111})
                .charPrimitiveArray(new char[]{'a','b','c'})
                .shortPrimitiveArray(new short[]{2,22,222})
                .intPrimitiveArray(new int[]{3,33,333})
                .longPrimitiveArray(new long[]{4L,44L,444L})
                .floatPrimitiveArray(new float[]{5.5F,5.55F,5.555F})
                .doublePrimitiveArray(new double[]{6.6D,6.66D,6.666D})
                .booleanPrimitiveArray(new boolean[]{true, false, true})
                .build();

        checkEqualsJsonObject(arraysPrimitiveTypeExample, arraysPrimitiveTypeExample, ArraysPrimitiveTypeExample.class);
    }

    @Test
    void complexObjectATest() {
        checkEqualsJsonObject(new A(), new A(), A.class);
    }

    @Test
    void complexObjectBTest() {
        checkEqualsJsonObject(new B(), new B(), B.class);
    }

    @Test
    void complexObjectCTest() {
        checkEqualsJsonObject(new C(), new C(), C.class);
    }

    @Test
    void complexObjectZTest() {
        checkEqualsJsonObject(new Z(), new Z(), Z.class);
    }

    private void checkEqualsJsonObject(Object object1, Object object2, Class<?> clazz) {
        JsonConverter converter = new JsonConverterImpl();
        Gson gson = new Gson();

        String converted = converter.toJson(object1);
        logger.info("converted = {}", converted);

        String convertedGson = gson.toJson(object2);
        logger.info("convertedAGson = {}", convertedGson);

        try {
            Object fromGsonJson =  gson.getAdapter(clazz).fromJson(convertedGson);
            logger.info("fromGsonJson = {}", fromGsonJson);

            Object fromMyJson =  gson.getAdapter(clazz).fromJson(converted);
            logger.info("fromMyJson = {}", fromMyJson);

            assertEquals(fromGsonJson, fromMyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    private class A {
        private String text = "some text";
    }

    @Data
    private class B {
        private Character ch = 'x';
    }

    @Data
    private class C {
        private A a = new A();
        private B b = new B();
    }

    @Data
    private class Z {
        private Map<String, String> ss = new HashMap<>();
        private Map<String, Object> s = new HashMap<>();
        private List<C> cc = new ArrayList<>();
        private Collection<C> collection = new LinkedList<>();

        Z() {
            ss.put("s1", "v1");
            ss.put("s2", "v2");
            ss.put("s3", "v3");

            cc.addAll(Arrays.asList(new C(), new C()));

            s.put("qwe", new A());
            s.put("rty", new B());
            s.put("qwerty", new C());
        }
    }
}