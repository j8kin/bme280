package bme280;

public enum mode {
    SLEEP(0b00), FORCED(0b01), NORMAL(0b11);
    private final byte byteVal;

    private mode(int byteVal) {
        assert byteVal >= 0b00 && byteVal <= 0b11;
        this.byteVal = (byte) byteVal;
    }

    public byte toByte() {
        return byteVal;
    }
}