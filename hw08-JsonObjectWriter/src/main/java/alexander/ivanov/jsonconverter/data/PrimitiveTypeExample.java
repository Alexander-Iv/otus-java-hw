package alexander.ivanov.jsonconverter.data;

public class PrimitiveTypeExample {
    private byte byte1;
    private char char1;
    private short short1;
    private int int1;
    private long long1;
    private float float1;
    private double double1;
    private boolean boolean1;

    public PrimitiveTypeExample(byte byte1, char char1, short short1, int int1, long long1, float float1, double double1, boolean boolean1) {
        this.byte1 = byte1;
        this.char1 = char1;
        this.short1 = short1;
        this.int1 = int1;
        this.long1 = long1;
        this.float1 = float1;
        this.double1 = double1;
        this.boolean1 = boolean1;
    }

    @Override
    public String toString() {
        return "PrimitiveTypeExample{" +
                "byte1=" + byte1 +
                ", char1=" + char1 +
                ", short1=" + short1 +
                ", int1=" + int1 +
                ", long1=" + long1 +
                ", float1=" + float1 +
                ", double1=" + double1 +
                ", boolean1=" + boolean1 +
                '}';
    }
}
