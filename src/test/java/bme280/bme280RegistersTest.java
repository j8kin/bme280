package bme280;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jdk.dio.i2cbus.I2CDevice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class bme280RegistersTest {

    private final I2CDevice device;
    private final bme280Registers registers;

    bme280RegistersTest() {
        device = Mockito.mock(I2CDevice.class);
        registers = new bme280Registers(device);
    }

    /**
     * Rigorous Test.
     */
    @Test
    void testGetIdBigEndian() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(device.read(0xD0, 1, 0, dest)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).put((byte) 0x60);
                    return 1;
                });
        }
        catch (Exception e) {
            fail("Device could not be read");
        }
        assertEquals(0x60, registers.getId());
    }

    @Test
    void testGetIdLittleEndian() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(device.read(0xD0, 1, 0, dest)).thenAnswer(
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
        assertEquals(0x60, registers.getId());
    }

    @Test
    void testResetBigEndian() {
        try {
            ByteBuffer src = ByteBuffer.allocateDirect(8).put((byte)0xB6).rewind();
            Mockito.when(device.write(0xE0, 1, src)).thenAnswer(
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

        registers.reset();
        assertTrue(true, "Register written successfully");
    }

    @Test
    void testGetHumidityControl() {
        try {
            ByteBuffer dest = ByteBuffer.allocateDirect(1);

            Mockito.when(device.read(0xF2, 1, 0, dest)).thenAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((ByteBuffer)args[3]).order(ByteOrder.BIG_ENDIAN).put((byte)0b110).rewind();

                    return 1;
                });

                ByteBuffer src = ByteBuffer.allocateDirect(8).put(oversampling.OVERSAMPLING8.toByte()).rewind();
                Mockito.when(device.write(0xF2, 1, src)).thenAnswer(
                    invocation -> {
                        Object[] args = invocation.getArguments();
                    
                        assertEquals(oversampling.OVERSAMPLING8.toByte(), ((ByteBuffer)args[2]).rewind().get());
    
                        return 1;
                    });
            }
        catch (Exception e) {
            fail("Device could not be read");
        }
        registers.setHumidityControl(oversampling.OVERSAMPLING8);

        assertTrue(true, "Humidity Control set successfully");
    }
}