package bme280;

/**
 * Calibration parameters.
 */
enum Bme280Calibration {
    /**
     * Temperature Calibration Parameter 1 (unsigned 16 bit integer).
     */
    digT1,
    /**
     * Temperature Calibration Parameter 2 (signed 16 bit integer).
     */
    digT2,
    /**
     * Temperature Calibration Parameter 3 (signed 16 bit integer).
     */
    digT3,

    /**
     * Pressure Calibration Parameter 1 (unsigned 16 bit integer).
     */
    digP1,
    /**
     * Pressure Calibration Parameter 2 (signed 16 bit integer).
     */
    digP2,
    /**
     * Pressure Calibration Parameter 3 (signed 16 bit integer).
     */
    digP3,
    /**
     * Pressure Calibration Parameter 4 (signed 16 bit integer).
     */
    digP4,
    /**
     * Pressure Calibration Parameter 5 (signed 16 bit integer).
     */
    digP5,
    /**
     * Pressure Calibration Parameter 6 (signed 16 bit integer).
     */
    digP6,
    /**
     * Pressure Calibration Parameter 7 (signed 16 bit integer).
     */
    digP7,
    /**
     * Pressure Calibration Parameter 8 (signed 16 bit integer).
     */
    digP8,
    /**
     * Pressure Calibration Parameter 9 (signed 16 bit integer).
     */
    digP9,

    /**
     * Humidity Calibration Parameter 1 (unsigned 8 bit).
     */
    digH1,
    /**
     * Humidity Calibration Parameter 2 (signed 16 bit integer).
     */
    digH2,
    /**
     * Humidity Calibration Parameter 3 (unsigned 8 bit).
     */
    digH3,
    /**
     * Humidity Calibration Parameter 4 (signed 16 bit integer).
     */
    digH4,
    /**
     * Humidity Calibration Parameter 5 (signed 16 bit integer).
     */
    digH5,
    /**
     * Humidity Calibration Parameter 6 (signed 8 bit integer).
     */
    digH6,

    /**
     * A fine resolution temperature value over to the pressure and humidity
     * compensation formula. This parameter is calculated in while calculating
     * temperature (signed 32 bit integer).
     */
    tFine;
}
