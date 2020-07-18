package bme280;

// import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;

public final class bme280 {

    I2CDeviceConfig bme280Config;

    /**
     * Default constructor which use device address 0x90
     */
    public bme280() {
        this(0x90);
    }

    /**
     * Public constructor
     * @param deviceId - bme280 device id (it could be 0x90 or tbd)
     */
    public bme280(int deviceId) {
        bme280Config = new I2CDeviceConfig.Builder()
                        .setControllerNumber(1) // todo
                        .setAddress(deviceId, I2CDeviceConfig.ADDR_SIZE_10) //todo
                        .setClockFrequency(8000000) // todo
                        .build();
    }
};