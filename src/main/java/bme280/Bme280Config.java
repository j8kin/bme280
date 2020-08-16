package Bme280;

import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;

public class Bme280Config {
    private Oversampling humidity;
    private Oversampling temperature;
    private Oversampling pressure;
    private Filter filter;
    private Standby standby;
    private I2CDevice bme280Device;
    private I2CDeviceConfig bme280I2CDeviceConfig;
    private int bme280I2CDeviceId;


    public static final  class Builder {
        private  final Bme280Config instance = new Bme280Config();

        public Builder() {}

        /** 
         * Build BME280Config
         */
        public Bme280Config build() throws Exception {

            if (instance.bme280Device == null) {
                // trying to create bme280Device based on bme280I2CDeviceConfig or default configuration
                if (instance.bme280I2CDeviceConfig == null) {
                    instance.bme280I2CDeviceConfig = new I2CDeviceConfig.Builder()
                        .setControllerNumber(1) // todo
                        .setAddress(instance.bme280I2CDeviceId, I2CDeviceConfig.ADDR_SIZE_10) //todo
                        .setClockFrequency(8000000) // todo
                        .build();
                }
                // create I2CDevice instance
                instance.bme280Device = DeviceManager.open(instance.bme280I2CDeviceConfig);
            }
            return instance;
        }

        public Builder setOversampling(final SensorType sensor, final Oversampling oversampling) {
            if (sensor == SensorType.TEMPERATURE) {
                instance.temperature = oversampling;
            }
            if (sensor == SensorType.HUMIDITY) {
                instance.humidity = oversampling;
            }
            if (sensor == SensorType.PRESSURE) {
                instance.pressure = oversampling;
            }
            return this;
        }

        public Builder setFilter(final Filter filter) {
            instance.filter = filter;
            return this;
        }

        public Builder setStandBy(final Standby standby) {
            instance.standby = standby;
            return this;
        }

        public Builder setI2CDevice(final I2CDevice bme280Device) {
            instance.bme280Device = bme280Device;
            return this;
        }

        public Builder setI2CDeviceConfig(final I2CDeviceConfig bme280I2CDeviceConfig) {
            instance.bme280I2CDeviceConfig = bme280I2CDeviceConfig;
            return this;
        }

        /**
         * Setup BME280 device id on i2c bus to verify connect bme280 to i2c and execute
         * "sudo i2cdetect -y 1" BME280 is connected to 0x76 or 0x77
         * 
         * @param deviceId - 0x76 or 0x77
         * @return this {@code Builder} instance
         * @throws IllegalArgumentException - if deviceId is not 0x76 or 0x77
         */
        public Builder setBME280I2CDeviceId(final int deviceId) {
            if (deviceId != 0x76 || deviceId != 0x77) {
                throw new IllegalArgumentException();
            }
            instance.bme280I2CDeviceId = deviceId;
            return this;
        }
    }
    
    public Oversampling getHumidityControl() {
        return humidity;
    }
    public Oversampling getTemperatureControl() {
        return temperature;
    }
    public Oversampling getPressureControl() {
        return pressure;
    }
    public Filter getFilter() {
        return filter;
    }
    public Standby getStandby() {
        return standby;
    }

    public I2CDevice getI2cDevice() {
        return bme280Device;
    }

    /** 
     * private constructor. Builder should be used to create Bme280Config instance
     */
    private Bme280Config() { 
        bme280I2CDeviceId = 0x76;
        humidity = Oversampling.OVERSAMPLING1;
        temperature = Oversampling.OVERSAMPLING1;
        pressure = Oversampling.OVERSAMPLING1;
        filter = Filter.OFF;
        standby = Standby.MSEC1000; // 1 second by default
    }
}