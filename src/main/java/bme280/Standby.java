package bme280;

    // |------------|----------------------------------------|
    // |   Value    | T-Standby (ms)                         |
    // |------------|----------------------------------------|
    // |    000     | 0.5                                    |
    // |    001     | 62.5                                   |
    // |    010     | 125                                    |
    // |    011     | 250                                    |
    // |    100     | 500                                    |
    // |    101     | 1000                                   |
    // |    110     | 10 (???? may be it is 10 sec)          |
    // |    111     | 20 (???? may be it is 20 sec)          |
    // |------------|----------------------------------------|
public enum Standby {
    MSEC5(0b000), MSEC62_5(0b001), MSEC125(0b010), 
    MSEC250(0b011), MSEC500(0b100), MSEC1000(0b101),
    SEC10(0b110), SEC20(0b111);

    private final byte byteVal;

    private Standby(int byteVal) {
        this.byteVal = (byte) byteVal;
    }

    public static final Standby fromInteger(int value) {
        assert value >= 0b000 && value <= 0b111;
        switch (value) {
            case 0b000: return MSEC5;
            case 0b001: return MSEC62_5;
            case 0b010: return MSEC125;
            case 0b011: return MSEC250;
            case 0b100: return MSEC500;
            case 0b101: return MSEC1000;
            case 0b110: return SEC10;
            case 0b111: 
            default:
                return SEC20;
        }
    }

    public byte toByte() {
        return this.byteVal;
    }
}
