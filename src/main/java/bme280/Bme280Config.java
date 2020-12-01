package bme280;

import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;

/**
 * BME 280 sensor configuration.
 */
public final class Bme280Config {
    private Bme280Oversampling humidity;
    private Bme280Oversampling temperature;
    private Bme280Oversampling pressure;
    private Bme280Filter filter;
    private Bme280Standby standby;
    private I2CDevice bme280Device;
    private I2CDeviceConfig bme280I2CDeviceConfig;
    private int bme280I2CDeviceId;

    /**
     * private constructor. Builder should be used to create Bme280Config instance.
     */
    private Bme280Config() {
        bme280I2CDeviceId = 0x76;
        humidity = Bme280Oversampling.OVERSAMPLING1;
        temperature = Bme280Oversampling.OVERSAMPLING1;
        pressure = Bme280Oversampling.OVERSAMPLING1;
        filter = Bme280Filter.OFF;
        standby = Bme280Standby.MSEC1000; // 1 second by default
    }

    /**
     * Sensor configuration class builder.
     */
    public static final class Builder {
        private final Bme280Config instance = new Bme280Config();

        /**
         * Default constructor.
         */
        public Builder() {
        }

        /**
         * Build BME280Config.
         */
        public Bme280Config build() throws Exception {

            if (instance.bme280Device == null) {
                // trying to create bme280Device based on bme280I2CDeviceConfig or default
                // configuration
                if (instance.bme280I2CDeviceConfig == null) {
                    instance.bme280I2CDeviceConfig = new I2CDeviceConfig.Builder().setControllerNumber(1) // todo
                            .setAddress(instance.bme280I2CDeviceId, I2CDeviceConfig.ADDR_SIZE_10) // todo
                            .setClockFrequency(8000000) // todo
                            .build();
                }
                // create I2CDevice instance
                instance.bme280Device = DeviceManager.open(instance.bme280I2CDeviceConfig);
            }
            return instance;
        }

        /**
         * Set sensor oversampling.
         * @param sensor - sensor type (Temperature/Humidity or Pressure)
         * @param oversampling - new oversampling value
         */
        public Builder setOversampling(final Bme280SensorType sensor, final Bme280Oversampling oversampling) {
            if (sensor == Bme280SensorType.TEMPERATURE) {
                instance.temperature = oversampling;
            }
            if (sensor == Bme280SensorType.HUMIDITY) {
                instance.humidity = oversampling;
            }
            if (sensor == Bme280SensorType.PRESSURE) {
                instance.pressure = oversampling;
            }
            return this;
        }

        /**
         * Set sensor filter.
         * @param filter - new sensor filter value
         */
        public Builder setFilter(final Bme280Filter filter) {
            instance.filter = filter;
            return this;
        }

        /**
         * Set sensor standby mode.
         * @param standby - new sensor standby mode
         */
        public Builder setStandBy(final Bme280Standby standby) {
            instance.standby = standby;
            return this;
        }

        /**
         * Set I2C Device.
         * @param bme280Device - i2c bme280 device
         */
        public Builder setI2CDevice(final I2CDevice bme280Device) {
            instance.bme280Device = bme280Device;
            return this;
        }

        /**
         * Set BME280 i2c device configuration.
         * @param bme280I2CDeviceConfig - new i2c device configuration
         * @return builder
         */
        public Builder setI2CDeviceConfig(final I2CDeviceConfig bme280I2CDeviceConfig) {
            instance.bme280I2CDeviceConfig = bme280I2CDeviceConfig;
            return this;
        }

        /**
         * Setup BME280 device id on i2c bus to verify connect bme280 to i2c and execute
         * "sudo i2cdetect -y 1" BME280 is connected to 0x76 or 0x77.
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

    /**
     * Get humidity control oversmpling.
     * @return current humidity oversampling
     */
    public Bme280Oversampling getHumidityControl() {
        return humidity;
    }

    /**
     * Get temperature control oversmpling.
     * @return current temperature oversampling
     */
    public Bme280Oversampling getTemperatureControl() {
        return temperature;
    }

    /**
     * Get pressure control oversmpling.
     * @return current pressure oversampling
     */
    public Bme280Oversampling getPressureControl() {
        return pressure;
    }

    /**
     * Get filtering mode.
     * @return current filter
     */
    public Bme280Filter getFilter() {
        return filter;
    }

    /**
     * Get current sensor standby mode.
     * @return current standby mode
     */
    public Bme280Standby getStandby() {
        return standby;
    }

    /**
     * Get BME 280 i2c device.
     * @return BME 280 i2c device
     */
    public I2CDevice getI2cDevice() {
        return bme280Device;
    }
}
