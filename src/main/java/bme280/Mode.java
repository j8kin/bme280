package Bme280;

public enum Mode {
    SLEEP(0b00), FORCED(0b01), NORMAL(0b11);
    private final byte byteVal;

    private Mode(int byteVal) {
        assert byteVal >= 0b00 && byteVal <= 0b11;
        this.byteVal = (byte) byteVal;
    }

    public byte toByte() {
        return byteVal;
    }
}
