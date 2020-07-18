package bme280;

import java.nio.ByteBuffer;

class bme280Calibration {
    // java int is 32 bit signed
    // short is 16 bit signed
    //https://en.wikibooks.org/wiki/Java_Programming/Primitive_Types#:~:text=Primitive%20types%20are%20the%20most,simple%20values%20of%20a%20kind.

    /* Temperature Calibration Parameters */
    private static int digT1; // used as unsigned 16 bit integer
    private static short digT2; // signed 16 bit integer
    private static short digT3; // signed 16 bit integer

    /* Pressure Calibration Parameters */
    private static int digP1; // used as unsigned 16 bit integer
    private static short digP2; // signed 16 bit integer
    private static short digP3; // signed 16 bit integer
    private static short digP4; // signed 16 bit integer
    private static short digP5; // signed 16 bit integer
    private static short digP6; // signed 16 bit integer
    private static short digP7; // signed 16 bit integer
    private static short digP8; // signed 16 bit integer
    private static short digP9; // signed 16 bit integer

    /* Humidity Calibration Parameters */
    private static short digH1; // used as unsigned 8 bit
    private static short digH2; // signed 16 bit integer
    private static short digH3; // used as unsigned 8 bit
    private static short digH4; // signed 16 bit integer
    private static short digH5; // signed 16 bit integer
    private static byte digH6; // signed 8 bit integer

    /* Unknown Parameter */
    private static int tFine; // used as signed 32 bit

    public static int getDigT1() {return digT1;}
    public static short getDigT2() {return digT2;}
    public static short getDigT3() {return digT3;}

    public static int getDigP1() {return digP1;}
    public static short getDigP2() {return digP2;}
    public static short getDigP3() {return digP3;}
    public static short getDigP4() {return digP4;}
    public static short getDigP5() {return digP5;}
    public static short getDigP6() {return digP6;}
    public static short getDigP7() {return digP7;}
    public static short getDigP8() {return digP8;}
    public static short getDigP9() {return digP9;}

    public static short getDigH1() {return digH1;}
    public static short getDigH2() {return digH2;}
    public static short getDigH3() {return digH3;}
    public static short getDigH4() {return digH4;}
    public static short getDigH5() {return digH5;}
    public static byte getDigH6() {return digH6;}

    public static int getTFine() {return tFine;}

    public void setTemperatureCalibrationParameters(ByteBuffer buffer) 
    {

    };
    public void setHumidityCalibrationParameters(ByteBuffer buffer) 
    {

    };
    public void setPressureCalibrationParameters(ByteBuffer buffer) 
    {
        
    };
}