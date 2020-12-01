package bme280;

/**
 * |------------|----------------------------------------|
 * |   Value    | T-Standby (ms)                         |
 * |------------|----------------------------------------|
 * |    000     | 0.5                                    |
 * |    001     | 62.5                                   |
 * |    010     | 125                                    |
 * |    011     | 250                                    |
 * |    100     | 500                                    |
 * |    101     | 1000                                   |
 * |    110     | 10 (???? may be it is 10 sec)          |
 * |    111     | 20 (???? may be it is 20 sec)          |
 * |------------|----------------------------------------|
 */
public enum Bme280Standby {
    /**
     * Sensor standby by 5 milliseconds.
     */
    MSEC5(0b000),
    /**
     * Sensor standby by 62.5 milliseconds.
     */
    MSEC62_5(0b001),
    /**
     * Sensor standby by 125 milliseconds.
     */
    MSEC125(0b010),
    /**
     * Sensor standby by 250 milliseconds.
     */
    MSEC250(0b011),
    /**
     * Sensor standby by 500 milliseconds.
     */
    MSEC500(0b100),
    /**
     * Sensor standby by 1000 milliseconds (1 second).
     */
    MSEC1000(0b101),
    /**
     * Sensor standby by 10 seconds.
     */
    SEC10(0b110),
    /**
     * Sensor standby by 20 seconds.
     */
    SEC20(0b111);

    /**
     * Byte representation of Standby.
     */
    private final byte byteVal;
    /**
     * Default constructor.
     */
    Bme280Standby(int byteVal) {
        this.byteVal = (byte) byteVal;
    }

    /**
     * Convert integer to Standy value.
     * @param value - input value
     * @return - value in Standby Enum
     */
    public static Bme280Standby fromInteger(int value) {
        assert value >= MSEC5.byteVal && value <= SEC20.byteVal;
        switch (value) {
            case 0b000:
                return MSEC5;
            case 0b001:
                return MSEC62_5;
            case 0b010:
                return MSEC125;
            case 0b011:
                return MSEC250;
            case 0b100:
                return MSEC500;
            case 0b101:
                return MSEC1000;
            case 0b110:
                return SEC10;
            case 0b111:
            default:
                return SEC20;
        }
    }

    /**
     * Get Standby value in byte representation.
     */
    public byte toByte() {
        return this.byteVal;
    }
}
