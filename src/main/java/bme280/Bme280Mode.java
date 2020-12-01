package bme280;

/**
 * BME 280 Mode.
 */
public enum Bme280Mode {
    /**
     * BME 280 in sleep mode.
     */
    SLEEP(0b00),
    /**
     * BME 280 in forced mode (get values immidiatly).
     */
    FORCED(0b01),
    /**
     * BME 280 in normal mode.
     */
    NORMAL(0b11);

    /**
     * Byte representation of BME 280 mode.
     */
    private final byte byteVal;

    /**
     * Default constructor.
     * @param byteVal - initial value
     */
    Bme280Mode(int byteVal) {
        assert byteVal >= 0b00 && byteVal <= 0b11;
        this.byteVal = (byte) byteVal;
    }

    /**
     * Return BME 280 mode in byte representation.
     * @return mode in byte representation
     */
    public byte toByte() {
        return byteVal;
    }
}
