package bme280;

//|------------|----------------------------------------|
//|   Value    | Filter coefficient                     |
//|------------|----------------------------------------|
//|    000     | Filter off                             |
//|    001     | 2                                      |
//|    010     | 4                                      |
//|    011     | 8                                      |
//|100, other  | 16                                     |
//|------------|----------------------------------------|
public enum filter {
    OFF(0b000), C2(0b001), C4(0b010), C8(0b011), C16(0b100);

    private final byte byteVal;

    private filter(int byteVal) {
        this.byteVal = (byte) byteVal;
    }

    public static final filter fromInteger(int value) {
        switch(value) {
            case 0b000: return OFF;
            case 0b001: return C2;
            case 0b010: return C4;
            case 0b011: return C8;
            case 0b100:
            default:
                return C16;
        }
    }

    public byte toByte() {
        return this.byteVal;
    }
}