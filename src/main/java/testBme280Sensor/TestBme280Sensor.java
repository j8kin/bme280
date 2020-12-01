package testBme280Sensor;

import bme280.Bme280Config;
import bme280.Bme280Sensor;

/**
 * Integration test for BME 280 Sensor class.
 */
public final class TestBme280Sensor {

    /** 
     * Test Sensor.
     */
    private final Bme280Sensor bme280Sensor;

    /**
     * Default constructor which use device address 0x76.
     */
    public TestBme280Sensor() throws Exception {
        this(0x76);
    }

    /**
     * Public constructor.
     * 
     * @param deviceId - bme280 device id (it could be 0x90 or tbd)
     */
    public TestBme280Sensor(int deviceId) throws Exception {
        // use default I2C BME280 device config
        bme280Sensor = new Bme280Sensor(new Bme280Config.Builder().build());
    }

    /**
     * Integration test for BME 280 Sensor.
     */
    public static void main(String[] args) {
        try {
            TestBme280Sensor testSensor = new TestBme280Sensor();
            System.out.println("Read temperature: " + testSensor.bme280Sensor.getRawTemperature());
            System.out.println("Read humidity: " + testSensor.bme280Sensor.getRawHumidity());
            System.out.println("Read pressure: " + testSensor.bme280Sensor.getRawPressure());
        } catch (Exception e) {
            System.out.println("Unhandled exception:\n" + e.toString());
        }
    }
}
