package bme280;

/**
 * Sensor data oversampling.
 * Oversampling Table:
 * |-------|-------------------------------------|
 * | Value | Humidity/Temp/Pressure oversampling |
 * |-------|-------------------------------------|
 * |  000  | Skipped (output set to 0x8000)      |
 * |  001  | oversampling x 1                    |
 * |  010  | oversampling x 2                    |
 * |  011  | oversampling x 4                    |
 * |  100  | oversampling x 8                    |
 * |  101  | oversampling x 16                   |
 * | other | oversampling x 16                   |
 * |-------|-------------------------------------|
 */
public enum Bme280Oversampling {
    /**
     * Skip oversampling.
     */
    SKIPPED(0b000),
    /**
     * Oversampling x 1.
     */
    OVERSAMPLING1(0b001),
    /**
     * Oversampling x 2.
     */
    OVERSAMPLING2(0b010),
    /**
     * Oversampling x 4.
     */
    OVERSAMPLING4(0b011),
    /**
     * Oversampling x 8.
     */
    OVERSAMPLING8(0b100),
    /**
     * Oversampling x 16.
     */
    OVERSAMPLING16(0b101);

    /**
     * Oversampling representation in byte.
     */
    private final byte byteVal;

    /**
     * Default constructor.
     * @param byteVal - input value
     */
    Bme280Oversampling(int byteVal) {
        assert byteVal >= 0b000 && byteVal <= 0b111;
        this.byteVal = (byte) byteVal;
    }

    /**
     * Convert integer value into Oversampling Enum.
     * @param value - input value
     */
    public static Bme280Oversampling fromInteger(int value) {
        switch (value) {
            case 0b000:
                return SKIPPED;
            case 0b001:
                return OVERSAMPLING1;
            case 0b010:
                return OVERSAMPLING2;
            case 0b011:
                return OVERSAMPLING4;
            case 0b100:
                return OVERSAMPLING8;
            case 0b101:
            default:
                return OVERSAMPLING16;
        }
    }

    /**
     * Return oversampling in byte representation.
     */
    public byte toByte() {
        return this.byteVal;
    }
}
