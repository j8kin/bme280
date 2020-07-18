package bme280;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jdk.dio.i2cbus.I2CDevice;

/**
 * Register description. Section 5 from bme280 datasheet (BST-BME280-DS002.pdf)
 */
public class bme280Registers {
    private final static Logger logger = LogManager.getLogger(bme280Registers.class);
    private I2CDevice device;

    public bme280Registers(I2CDevice bme280) {
        device = bme280;
    }

    /**
     * Convert Byte Buffer to long (8 byte???)
     * @param buffer - buffer to convert
     * @return - converted value
     */
    private long byteBufferToLong(ByteBuffer buffer) {
        long result = 0L; 
        final int size = buffer.position();

        buffer.rewind();
        
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = 0; i < size; i++) {
                result += buffer.get()*(long)Math.pow(2, 8*i);
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                result += buffer.get()*(long)Math.pow(2, 8*(size-i-1));
            }           
        }
        return result;
    }

    /**
     * Convert long (8 byte ???) to ByteBuffer
     * @param value - long value
     * @return - converted value
     */
    private ByteBuffer longToByteBuffer(final long value) {
        ByteBuffer result = ByteBuffer.allocateDirect(8); // ??? allocate to size of long
        for (int i = 0; i < 8; i++) {
            if (result.order() == ByteOrder.BIG_ENDIAN) {
                result.put((byte)((value >> 8*i) & 0x00FF));
            }
            else {
                result.put((byte)((value & (0xFF << 8*(8-i-1)))>>8*(8-i-1))); // 8-i-1 size of long???
            }
        }
        result.rewind();
        return result;
    }

    /**
     * Read one byte of data from Register
     * @param register - register of BME280 to be read (0xD0, 0xE0, etc)
     * @return bme280 register value
     */
    private long readRegister(int register) {
        return readRegister(register, 1);
    }

    /**
     * Read number of bytes from bme280 register
     * @param register - register to be read
     * @param size - number of bytes to be read
     * @return bme280 register data
     */
    private long readRegister(int register, int size) {
        try {
            // assert based on Table 18: memory map from BME280 datasheet
            assert (register >= 0x88 && register <= 0xA1) || // calibration data
                    register == 0xD0 ||                      // chip id (should be 0x60)
                    (register >= 0xE1 && register <=0xF0) || // calibration data
                    register == 0xF2 ||                      // humidity control
                    register == 0xF3 ||                      // status
                    register == 0xF4 ||                      // temperature, pressure controls + mode
                    register == 0xF5 ||                      // configuration
                    register == 0xF7 ||                      // pressure MSB
                    register == 0xF8 ||                      // pressure LSB
                    register == 0xF9 ||                      // pressure XLSB (bits [7:4]) + 0 (bits [3:0])
                    register == 0xFA ||                      // temperature MSB
                    register == 0xFB ||                      // temperature LSB
                    register == 0xFC ||                      // temperature XLSB (bits [7:4]) + 0 (bits [3:0])
                    register == 0xFD ||                      // humidity MSB
                    register == 0xFE                         // humidity LSB
                    ;
            ByteBuffer dst = ByteBuffer.allocateDirect(size);
            int nBytesRead = device.read(register, size, 0, dst);

            assert nBytesRead == size;

            return byteBufferToLong(dst); 
        }
        catch (Exception e) {
            logger.atError().withThrowable(e).log("Unable to readRegister request.");
        }
        return -1;
    }

    /**
     * Write one byte of data into bme280 register
     * @param register - register to be write (0xF5, 0xF4, 0xF2, 0xE0)
     * @param data - data to be written (one byte)
     */
    private void writeRegister(int register, long data) {
        writeRegister(register, data, 1);
    }

    /**
     * Write up to 8 bytes
     * @param register - register to be write (0xF5, 0xF4, 0xF2, 0xE0)
     * @param data - up to 8 bytes to be written
     * @param size - size (1 to 8)
     */
    private void writeRegister(int register, long data, int size) {
        try {
            assert register == 0xF5 || register == 0xF4 || register == 0xF2 || register == 0xE0;
            ByteBuffer src = longToByteBuffer(data);

            int nBytesWrite = device.write(register, size, src);
            assert nBytesWrite == size;
        }
        catch (Exception e) {
            logger.atError().withThrowable(e).log("Unable to writeRegister request.");
        }
    }

    // The "id" register (0xD0) contains chip identification number chip_id[7:0], which is 0x60.
    // This numder can be read as soon as device finished the power-on-reser
    public long getId() {
        long id = readRegister(0xD0);
        assert id == 0x60;
        return id;
    }

    // The "reset" register (0xE0) contains the soft reset word reset [7:0]. 
    // If the value 0xB6 is written to the register, the device is reset using the complete power-on reset procedure.
    // Writing other values then 0xB6 has no effect,
    // The readout value is always 0x00
    public void reset() {
        writeRegister(0xE0, 0xB6);
    }
    

    // The "humidityControl" register sets the humidity data acquisition of the device
    // Attention!!! 
    // Changes to this register only become effective after a write operation to measurmentControl
    // Only bits 2:0 are used as oversampling (see Oversampling Table above)
    private oversampling getHumidityControl() {
        return oversampling.fromInteger(((int)readRegister(0xF2) & 0x07));
    }

    public void setHumidityControl(oversampling oversampling) {
        long data = readRegister(0xF2) & 0xF8; // set humidity control bits to zero
        data |= oversampling.toByte(); // place temperature oversampling into bits [2:0]
        writeRegister(0xF2, data);
    }


    // The "Status" register contains two bits which indicates the status of the device
    // |-----------|--------------|------------------------------------------------------|
    // |  bit #    | Name         | Descrition                                           |
    // |-----------|--------------|------------------------------------------------------|
    // |    3      | measuring[0] | Automatically set to '1' whenever a conversion is    |
    // |           |              | running and back to '0' when the result have been    |
    // |           |              | transfered to the data registers.                    |
    // |-----------|--------------|------------------------------------------------------|
    // |    0      | im_update[0] | Automatically set to '1' when the NVM data are being |
    // |           |              | copied to image registers and back to '0' when the   |
    // |           |              | copying is done. The data are copied at power-on-    |
    // |           |              | reset and before every conversion.                   |
    // |-----------|--------------|------------------------------------------------------|
    private int getMesuringStatus() {
        return (int) (readRegister(0xF3) & 0x08) >> 3;
    }
    private int getImUpdateStatus() {
        return (int) readRegister(0xF3) & 0x01;
    }

    // The "Control Mesurments" register sets the pressure and temperature data aquisition 
    //   options of the device. 
    // Attention !!!
    // The register needs to be written after changing HUMIDITY_CONTROL for the changes 
    //   to become effective.
    // |-----------|--------------|------------------------------------------------------|
    // |  bits #   | Name         | Descrition                                           |
    // |-----------|--------------|------------------------------------------------------|
    // |  7,6,5    | osrs_t[2:0]  | Control oversampling of temperature data. See above. |
    // |  4,3,2    | osrs_p[2:0]  | Control oversampling of pressure data. See above.    |
    // |  1,0      | mode[1:0]    | Control the sensor mode of the device:               |
    // |           |              | ------------|----------------------------------------|
    // |           |              |  Value      | Mode                                   |
    // |           |              | ------------|----------------------------------------|
    // |           |              |     00      | Sleep Mode                             |
    // |           |              |  01 and 10  | Forced Mode                            |
    // |           |              |     11      | Normal Mode                            |
    // |-----------|--------------|------------------------------------------------------|
    private oversampling getTemperatureControl() {
        return oversampling.fromInteger((int) ((readRegister(0xF4) & 0xE0) >> 5));
    }

    private oversampling getPressureControl() {
        return oversampling.fromInteger((int)((readRegister(0xF4) & 0x1C) >> 2));
    }

    private mode getModeControl() {
        switch((byte) (readRegister(0xF4) & 0x03)) {
            case 0b00: 
                return mode.SLEEP;
            case 0b01:
            case 0b10:
                return mode.FORCED;
            case 0b11:
            default:
                return mode.NORMAL;
        }
    }

    public void setTemperatureControl(oversampling oversampling) {
        long data = readRegister(0xF4) & 0x01F; // store only current pressure control and mode
        data |= oversampling.toByte() << 5; // place temperature oversampling into bits [7:5]
        writeRegister(0xF4, data);
    }

    public void setPressureControl(oversampling oversampling) {
        long data = readRegister(0xF4) & 0x0E3; // store only current temperature control and mode
        data |= oversampling.toByte() << 2; // place temperature oversampling into bits [4:2]
        writeRegister(0xF4, data);
    }

    public void setMode(mode mode) {
        long data = readRegister(0xF4) & 0x0FC; // store only current temperature and humidity control 
        data |= mode.toByte(); // place temperature oversampling into bits [1:0]
        writeRegister(0xF4, data);
    }

    // The "Config" register set the rate, filter and interface options of the device.
    // Writes to the "Config" register in normal mode may be ignored. 
    // In Sleep mode writes are NOT ignored
    // |-----------|--------------|------------------------------------------------------|
    // |  bits #   | Name         | Descrition                                           |
    // |-----------|--------------|------------------------------------------------------|
    // |   7,6,5   | t_sb[2:0]    | Control inactive duration t-standby in normal mode:  |
    // |           |              | ------------|----------------------------------------|
    // |           |              |  Value      | T-Standby (ms)                         |
    // |           |              | ------------|----------------------------------------|
    // |           |              |     000     | 0.5                                    |
    // |           |              |     001     | 62.5                                   |
    // |           |              |     010     | 125                                    |
    // |           |              |     011     | 250                                    |
    // |           |              |     100     | 500                                    |
    // |           |              |     101     | 1000                                   |
    // |           |              |     110     | 10 (???? may be it is 10 sec)          |
    // |           |              |     111     | 20 (???? may be it is 20 sec)          |
    // |-----------|--------------|------------------------------------------------------|
    // |   4,3,2   | filter[2:0]  | Controls the time constant of IIR filer              |
    // |           |              | ------------|----------------------------------------|
    // |           |              |  Value      | Filter coefficient                     |
    // |           |              | ------------|----------------------------------------|
    // |           |              |     000     | Filter off                             |
    // |           |              |     001     | 2                                      |
    // |           |              |     010     | 4                                      |
    // |           |              |     011     | 8                                      |
    // |           |              | 100, other  | 16                                     |
    // |-----------|--------------|------------------------------------------------------|
    // |     0     | spi3w_en[0]  | Enables 3-wire SPI interface when set to 1           |
    // |-----------|--------------|------------------------------------------------------|
    private standby getStandby() {
        return standby.fromInteger((int)(readRegister(0xF5) & 0xE0) >> 5);
    }

    private filter getFilter() {
        return filter.fromInteger((int)(readRegister(0xF5) & 0x1C) >> 2);
    }

    private boolean getSpiEnabled() {
        return (readRegister(0xF5) & 0x01) == 1;
    }

    public void setStandby(standby value) {
        long data = readRegister(0xF5) & 0x01F; // store only filter coefficient and SPI Enabled 
        data |= value.toByte() << 5; // place temperature oversampling into bits [7:5]
        writeRegister(0xF5, data);
    }

    public void setFilter(filter value) {
        long data = readRegister(0xF5) & 0x0E3; // store only T-Standby and SPI Enabled 
        data |= value.toByte() << 2; // place temperature oversampling into bits [4:2]
        writeRegister(0xF5, data);
    }

    public void setFilter(boolean enable) {
        long data = readRegister(0xF5) & 0x0FE; // store only T-Standby and filter coefficient 
        data |= enable? 0b1 : 0b0; // place temperature oversampling into bits [4:2]
        writeRegister(0xF5, data);
    }

    // The "Pressure" registers contains raw pressure measurment output data up to [19:0].
    // For more info see Chapter 4 of BME datasheet
    public long getRawPressure() {
        // todo: to think how to use readRegister(0xF7, 3) - read all 3 bytes at once
        long msb = readRegister(0xF7);
        long lbs = readRegister(0xF8);
        long xlbs = readRegister(0xF9)>> 4;
        
        return (xlbs << 15) | (lbs << 7) | msb;
    }

    // The "Temperature" registers contains raw pressure measurment output data up to [19:0].
    // For more info see Chapter 4 of BME datasheet
    public long getRawTemperature() {
        // todo: to think how to use readRegister(0xFA, 3) - read all 3 bytes at once
        long msb = readRegister(0xFA);
        long lbs = readRegister(0xFB);
        long xlbs = readRegister(0xFC)>> 4;

        return (xlbs << 15) | (lbs << 7) | msb;
    }

    // The "Humidity" registers contains raw pressure measurment output data up to [15:0].
    // For more info see Chapter 4 of BME datasheet
    public long getRawHumidity() {
        // todo: to think how to use readRegister(0xFD, 2) - read all 2 bytes at once
        long msb = readRegister(0xFD);
        long lbs = readRegister(0xFE);

        return (lbs << 7) | msb;
    }
}