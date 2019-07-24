package alexander.ivanov.jsonconverter.impl;

import alexander.ivanov.jsonconverter.JsonConverter;
import alexander.ivanov.jsonconverter.data.ArraysPrimitiveTypeExample;
import alexander.ivanov.jsonconverter.data.PrimitiveTypeExample;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterImplTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonConverterImplTest.class);

    @Test
    void defaultPrimitivesToJson() {
        JsonConverter converter = new JsonConverterImpl();

        String converted = converter.toJson(PrimitiveTypeExample.builder().build());
        logger.info("converted = {}", converted);

        Gson gson = new Gson();
        String convertedGson = gson.toJson(PrimitiveTypeExample.builder().build());
        logger.info("convertedGson = {}", convertedGson);

        try {
            Object fromGsonJson =  gson.getAdapter(PrimitiveTypeExample.class).fromJson(convertedGson);
            logger.info("fromGsonJson = {}", fromGsonJson);

            Object fromMyJson =  gson.getAdapter(PrimitiveTypeExample.class).fromJson(converted);
            logger.info("fromMyJson = {}", fromMyJson);

            assertEquals(fromGsonJson, fromMyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        JsonConverter converter = new JsonConverterImpl();
        String converted = converter.toJson(arraysPrimitiveTypeExample);
        logger.info("converted = {}", converted);

        Gson gson = new Gson();
        String convertedGson = gson.toJson(arraysPrimitiveTypeExample);
        logger.info("convertedGson = {}", convertedGson);

        try {
            Object fromGsonJson =  gson.getAdapter(ArraysPrimitiveTypeExample.class).fromJson(convertedGson);
            logger.info("fromGsonJson = {}", fromGsonJson);

            Object fromMyJson =  gson.getAdapter(ArraysPrimitiveTypeExample.class).fromJson(converted);
            logger.info("fromMyJson = {}", fromMyJson);

            assertEquals(fromGsonJson, fromMyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}