package alexander.ivanov.jsonconverter.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArraysPrimitiveTypeExample {
    private byte[] bytePrimitiveArray = new byte[]{};
    private char[] charPrimitiveArray = new char[]{};
    private short[] shortPrimitiveArray = new short[]{};
    private int[] intPrimitiveArray = new int[]{};
    private long[] longPrimitiveArray = new long[]{};
    private float[] floatPrimitiveArray = new float[]{};
    private double[] doublePrimitiveArray = new double[]{};
    private boolean[] booleanPrimitiveArray = new boolean[]{};
}
