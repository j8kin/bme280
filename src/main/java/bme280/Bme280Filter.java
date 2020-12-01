package bme280;

/**
 * Filter coefficient enum.
 * |------------|----------------------------------------|
 * |   Value    | Filter coefficient                     |
 * |------------|----------------------------------------|
 * |    000     | Filter off                             |
 * |    001     | 2                                      |
 * |    010     | 4                                      |
 * |    011     | 8                                      |
 * |100, other  | 16                                     |
 * |------------|----------------------------------------|
 */
public enum Bme280Filter {
    /**
     * Filter is off.
     */
    OFF(0b000),
    /**
     * C2 filter is used.
     */
    C2(0b001),
    /**
     * C4 filter is used.
     */
    C4(0b010),
    /**
     * C8 filter is used.
     */
    C8(0b011),
    /**
     * C16 filter is used.
     */
    C16(0b100);

    /**
     * Filter value in byte representation.
     */
    private final byte byteVal;

    /**
     * Default constructor.
     * @param byteVal - Filter value in byte represenation.
     */
    Bme280Filter(int byteVal) {
        this.byteVal = (byte) byteVal;
    }

    /**
     * Get Filter value from input integer value.
     * @param value - input value
     * @return - Filter enum
     */
    public static Bme280Filter fromInteger(int value) {
        switch (value) {
            case 0b000:
                return OFF;
            case 0b001:
                return C2;
            case 0b010:
                return C4;
            case 0b011:
                return C8;
            case 0b100:
            default:
                return C16;
        }
    }

    /**
     * Return value in byte representation.
     */
    public byte toByte() {
        return this.byteVal;
    }
}
