package bme280;

import java.util.HashMap;
import java.util.Map;

import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;



public final class Bme280 {
    private final Bme280Sensor bme280Registres;
    private final Map<Calibration,Long> calibrationData  = new HashMap<>();
    private final Map<SensorType, Oversampling> oversampling = new HashMap<>();

    /**
     * Default constructor which use device address 0x90
     */
    public Bme280() throws Exception {
        this(0x76);
    }

    /**
     * Public constructor
     * @param deviceId - bme280 device id (it could be 0x90 or tbd)
     */
    public Bme280(int deviceId) throws Exception {
        // use default I2C BME280 device config
        final Bme280Config bme280Config = new Bme280Config.Builder().build();
        
        bme280Registres = new Bme280Sensor(bme280Config);    
    }

    /**
     * Setup Bme280 device
     */
    public void Setup() {

    }

};
