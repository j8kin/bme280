package Bme280;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.Map;

import jdk.dio.i2cbus.I2CDevice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class Bme280RegistersTest {

    private final I2CDevice mockI2CDevice;
    private Bme280Sensor bme280Sensor;

    Bme280RegistersTest() {
        mockI2CDevice = Mockito.mock(I2CDevice.class);

        ByteBuffer oneByte = ByteBuffer.allocateDirect(1);
        ByteBuffer eightBytes = ByteBuffer.allocateDirect(8);

        try {
            // Mock answer for Bme280Sensor.setHumidityControl
            Mockito.when(mockI2CDevice.read(0xF2, 1, 0, oneByte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).put((byte) 0);
                    return 1;
                });
            Mockito.when(mockI2CDevice.write(0xF2, 1, eightBytes.put((byte)0).rewind())).thenAnswer(
                invocation -> {                        
                    return 1;
                });
    
            // Mock answer for Bme280Sensor.setPressureControl or SetHumidityControl
            Mockito.when(mockI2CDevice.read(0xF4, 1, 0, oneByte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).put((byte) 0);
                    return 1;
                });
            Mockito.when(mockI2CDevice.write(0xF4, 1, eightBytes.put((byte)0).rewind())).thenAnswer(
                invocation -> {                        
                    return 1;
                });

            // Mock answer to Bme280Sensor.setFilter or setStandby
            Mockito.when(mockI2CDevice.read(0xF5, 1, 0, oneByte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).put((byte) 0);
                    return 1;
                });
            Mockito.when(mockI2CDevice.write(0xF5, 1, eightBytes.put((byte)0).rewind())).thenAnswer(
                invocation -> {                        
                    return 1;
                });

            bme280Sensor = new Bme280Sensor(
                new Bme280Config.Builder()
                .setOversampling(SensorType.TEMPERATURE, Oversampling.SKIPPED)
                .setOversampling(SensorType.PRESSURE, Oversampling.SKIPPED)
                .setOversampling(SensorType.HUMIDITY, Oversampling.SKIPPED)
                .setFilter(Filter.OFF)
                .setStandBy(Standby.MSEC5)
                .setI2CDevice(mockI2CDevice)
                .build());
        }

        catch (Exception e) {
            fail("Unable to initialize test");
        }
    }

    /**
     * Rigorous Test.
     */
    @Test
    void testGetIdBigEndian() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(mockI2CDevice.read(0xD0, 1, 0, dest)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).put((byte) 0x60);
                    return 1;
                });
        }
        catch (Exception e) {
            fail("Device could not be read");
        }
        assertEquals(0x60, bme280Sensor.getId());
    }

    @Test
    void testGetIdLittleEndian() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(mockI2CDevice.read(0xD0, 1, 0, dest)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).order(ByteOrder.LITTLE_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0x60);

                    return 1;
                });
        }
        catch (Exception e) {
            fail("Device could not be read");
        }
        assertEquals(0x60, bme280Sensor.getId());
    }

    @Test
    void testResetBigEndian() {
        try {
            ByteBuffer src = ByteBuffer.allocateDirect(8).put((byte)0xB6).rewind();
            Mockito.when(mockI2CDevice.write(0xE0, 1, src)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    // 0xB6 == 182 the significant byte is 1 since java dont have unsigned convert to int with (+ 256)
                    assertEquals(0xB6, 256 + ((ByteBuffer)args[2]).rewind().get());
                    
                    return 1;
                }
            );
        }
        catch (Exception e) {
            fail("Device could not be write");
        }

        bme280Sensor.reset();
        assertTrue(true, "Register written successfully");
    }

    @Test
    void testGetHumidityControl() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(mockI2CDevice.read(0xF2, 1, 0, dest)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN).put((byte)0b110).rewind();

                    return 1;
                });

                ByteBuffer src = ByteBuffer.allocateDirect(8).put(Oversampling.OVERSAMPLING8.toByte()).rewind();
                Mockito.when(mockI2CDevice.write(0xF2, 1, src)).thenAnswer(
                    invocation -> {
                        Object[] args = invocation.getArguments();
                    
                        assertEquals(Oversampling.OVERSAMPLING8.toByte(), ((ByteBuffer)args[2]).rewind().get());
    
                        return 1;
                    });
            }
        catch (Exception e) {
            fail("Device could not be read");
        }
        bme280Sensor.setHumidityControl(Oversampling.OVERSAMPLING8);

        assertTrue(true, "Humidity Control set successfully");
    }

    @Test
    void testGetCalibration() {
        try {
            ByteBuffer dest2Byte = ByteBuffer.allocateDirect(2);
            ByteBuffer dest1Byte = ByteBuffer.allocateDirect(1);

            // DigT1
            Mockito.when(mockI2CDevice.read(0x88, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0x88);
                    ((ByteBuffer)args[3]).put((byte) 0x89);
                    return 2;
                });
            // DigT2
            Mockito.when(mockI2CDevice.read(0x8A, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x8B8A === -29814
                    ((ByteBuffer)args[3]).put((byte) 0x8A);
                    ((ByteBuffer)args[3]).put((byte) 0x8B);
                    return 2;
                });
            // DigT3
            Mockito.when(mockI2CDevice.read(0x8C, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x8000 === -32768
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    ((ByteBuffer)args[3]).put((byte) 0x80);
                    return 2;
                });
            // DigP1
            Mockito.when(mockI2CDevice.read(0x8E, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    ((ByteBuffer)args[3]).put((byte) 0x7F);
                    return 2;
                });
            // DigP2
            Mockito.when(mockI2CDevice.read(0x90, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    return 2;
                });
            // DigP3
            Mockito.when(mockI2CDevice.read(0x92, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xFFFF = -1
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    return 2;
                });
            // DigP4
            Mockito.when(mockI2CDevice.read(0x94, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x0001 = 1
                    ((ByteBuffer)args[3]).put((byte) 0x01);
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    return 2;
                });
            // DigP5
            Mockito.when(mockI2CDevice.read(0x96, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xFF00 = -256
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    return 2;
                });
            // DigP6
            Mockito.when(mockI2CDevice.read(0x98, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x00FF = 255
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    return 2;
                });
            // DigP7
            Mockito.when(mockI2CDevice.read(0x9A, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x0FFF = 4095
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    ((ByteBuffer)args[3]).put((byte) 0x0F);
                    return 2;
                });
            // DigP8
            Mockito.when(mockI2CDevice.read(0x9C, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xBF6A = -28673
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    ((ByteBuffer)args[3]).put((byte) 0x8F);
                    return 2;
                });
            // DigP9
            Mockito.when(mockI2CDevice.read(0x9E, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xF000 = -4096
                    ((ByteBuffer)args[3]).put((byte) 0x00);
                    ((ByteBuffer)args[3]).put((byte) 0xF0);
                    return 2;
                });

            // DigH1
            Mockito.when(mockI2CDevice.read(0xA1, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xFF = 256 (since H1 is unsigned)
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    return 1;
                });

            // DigH2
            Mockito.when(mockI2CDevice.read(0xE1, 2, 0, dest2Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xFFFE = -2
                    ((ByteBuffer)args[3]).put((byte) 0xFE);
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    return 2;
                });
            // DigH3
            Mockito.when(mockI2CDevice.read(0xE3, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0xFF = 256 (since H1 is unsigned)
                    ((ByteBuffer)args[3]).put((byte) 0x80);
                    return 1;
                });
            // DigH4/H5
            // E4:E6 = [0xFF, 0x75, 0xAA] -> H4 == 0xFF5; H5 == 0xAA7
            Mockito.when(mockI2CDevice.read(0xE4, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0xFF);
                    return 1;
                });
            Mockito.when(mockI2CDevice.read(0xE5, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0x75);
                    return 1;
                });
            Mockito.when(mockI2CDevice.read(0xE6, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    ((ByteBuffer)args[3]).put((byte) 0xAA);
                    return 1;
                });
            // DigH6
            Mockito.when(mockI2CDevice.read(0xE7, 1, 0, dest1Byte)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments(); 
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN);
                    // return 0x80 = -128 (since H6 is signed)
                    ((ByteBuffer)args[3]).put((byte) 0x80);
                    return 1;
                });                
        }
        catch (Exception e) {
            fail("Device could not be read");
        }

        Map<Calibration,Long> calibration = bme280Sensor.getCalibrationParameters();

        assertEquals(0x8988, calibration.get(Calibration.digT1));
        assertEquals(-29814, calibration.get(Calibration.digT2));
        assertEquals(-32768, calibration.get(Calibration.digT3));

        assertEquals(0x7FFF, calibration.get(Calibration.digP1));
        assertEquals(0x0000, calibration.get(Calibration.digP2));
        assertEquals(-1, calibration.get(Calibration.digP3));
        assertEquals(0x0001, calibration.get(Calibration.digP4));
        assertEquals(-256, calibration.get(Calibration.digP5));
        assertEquals(0x00FF, calibration.get(Calibration.digP6));
        assertEquals(0x0FFF, calibration.get(Calibration.digP7));
        assertEquals(-28673, calibration.get(Calibration.digP8));
        assertEquals(-4096, calibration.get(Calibration.digP9));

        assertEquals(0xFF, calibration.get(Calibration.digH1));
        assertEquals(-2, calibration.get(Calibration.digH2));
        assertEquals(0x80, calibration.get(Calibration.digH3));
        assertEquals(0xFF5, calibration.get(Calibration.digH4));
        assertEquals(0xAA7, calibration.get(Calibration.digH5));
        assertEquals(-128, calibration.get(Calibration.digH6));
    }
}