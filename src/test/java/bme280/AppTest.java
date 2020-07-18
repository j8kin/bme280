package bme280;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.ByteBuffer;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        ByteBuffer dest = ByteBuffer.allocateDirect(10);
        dest.put((byte)0x60);
        dest.rewind();

        try {
            long res = dest.get();
            assertEquals(0x60, res);
        }
        catch (Exception e) {
            fail("unhandled exception");
        }
    }
}
