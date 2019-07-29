package alexander.ivanov.jsonconverter.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrimitiveTypeExample {
    private byte byte1;
    private char char1;
    private short short1;
    private int int1;
    private long long1;
    private float float1;
    private double double1;
    private boolean boolean1;
}
