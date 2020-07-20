package bme280;

import java.util.Map;

import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;



public final class Bme280 {
    private final Bme280Registers bme280Registres;
    private Map<Calibration,Long> calibrationData;

    /**
     * Default constructor which use device address 0x90
     */
    public Bme280() {
        this(0x90);
    }

    /**
     * Public constructor
     * @param deviceId - bme280 device id (it could be 0x90 or tbd)
     */
    public Bme280(int deviceId) {
        final I2CDeviceConfig bme280Config = new I2CDeviceConfig.Builder()
                        .setControllerNumber(1) // todo
                        .setAddress(deviceId, I2CDeviceConfig.ADDR_SIZE_10) //todo
                        .setClockFrequency(8000000) // todo
                        .build();

        try {
            final I2CDevice device = DeviceManager.open(bme280Config);
        
            bme280Registres = new Bme280Registers(device);    
        }
        catch (Exception e) {
            // todo ???
            bme280Registres = null;
        }
    }

    /**
     * Setup Bme280 device
     */
    public void Setup() {

    }

};