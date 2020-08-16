package bme280;

import jdk.dio.i2cbus.I2CDevice;

public class Bme289Config {
    private Oversampling humidity;
    private Oversampling temperature;
    private Oversampling pressure;
    private Filter filter;
    private Standby standby;
    private I2CDevice bme280Device;


    public static final  class Builder {
        private  final Bme289Config instance = new Bme289Config();

        public Builder() {}

        /**
         * @throw Bme289Config - when no BME280 device is setup
         */
        public Bme289Config build() {
            instance.checkConfig();
            return instance;
        }

        public Builder setOversampling(SensorType sensor, Oversampling oversampling) {
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

        public Builder setFilter(Filter filter) {
            instance.filter = filter;
            return this;
        }

        public Builder setStandBy(Standby standby) {
            instance.standby = standby;
            return this;
        }

        public Builder setI2CDevice(I2CDevice bme280Device) {
            instance.bme280Device = bme280Device;
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
    public Filter getStandby() {
        return standby;
    }

    /** 
     * private constructor. Builder should be used to create Bme280Config instance
     */
    private Bme289Config() {}

    private void checkConfig() {
        if (this.bme280Device == null) {
            throw new IllegalArgumentException();
        }
    }
}