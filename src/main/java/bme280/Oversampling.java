package bme280;

// Oversampling Table:
// |-------|-------------------------------------|
// | Value | Humidity/Temp/Pressure oversampling |
// |-------|-------------------------------------|
// |  000  | Skipped (output set to 0x8000)      |
// |  001  | oversampling x 1                    |
// |  010  | oversampling x 2                    |
// |  011  | oversampling x 4                    |
// |  100  | oversampling x 8                    |
// |  101  | oversampling x 16                   |
// | other | oversampling x 16                   |
// |-------|-------------------------------------|
public enum Oversampling {
    SKIPPED(0b000), OVERSAMPLING1(0b001), OVERSAMPLING2(0b010), OVERSAMPLING4(0b011), OVERSAMPLING8(0b100), OVERSAMPLING16(0b101);

    private final byte byteVal;

    private Oversampling(int byteVal) {
        assert byteVal >= 0b000 && byteVal <= 0b111;
        this.byteVal = (byte) byteVal;
    }

    public static final Oversampling fromInteger(int value) {
        switch(value) {
            case 0b000: return SKIPPED;
            case 0b001: return OVERSAMPLING1;
            case 0b010: return OVERSAMPLING2;
            case 0b011: return OVERSAMPLING4;
            case 0b100: return OVERSAMPLING8;
            case 0b101:
            default:
                return OVERSAMPLING16;
        }
    }

    public byte toByte() {
        return this.byteVal;
    }
}
