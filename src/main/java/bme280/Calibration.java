package bme280;

public enum Calibration {
    /* Temperature Calibration Parameters */
    /** unsigned 16 bit integer */
    digT1,
    /** signed 16 bit integer */
    digT2,
    /** signed 16 bit integer */
    digT3,
    /* Pressure Calibration Parameters */
    /** unsigned 16 bit integer */
    digP1,
    /** signed 16 bit integer */
    digP2,
    /** signed 16 bit integer */
    digP3,
    /** signed 16 bit integer */
    digP4,
    /** signed 16 bit integer */
    digP5,
    /** signed 16 bit integer */
    digP6,
    /** signed 16 bit integer */
    digP7,
    /** signed 16 bit integer */
    digP8,
    /** signed 16 bit integer */
    digP9,
    /* Humidity Calibration Parameters */
    /** unsigned 8 bit */
    digH1,
    /** signed 16 bit integer */
    digH2,
    /** unsigned 8 bit */
    digH3,
    /** signed 16 bit integer */
    digH4,
    /** signed 16 bit integer */
    digH5,
    /** signed 8 bit integer */
    digH6,

    /* Unknown Parameter */
    /** signed 32 bit integer */
    tFine;
}