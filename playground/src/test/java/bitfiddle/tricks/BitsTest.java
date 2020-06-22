package bitfiddle.tricks;


import org.junit.jupiter.api.Test;

import static bitfiddle.Bits.*;
import static bitfiddle.MoreInts.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitsTest {

    @Test
    public void single_bit_count() {
        int value1 = toInt("1");
        assertEquals(1, countBits(value1));
    }

    @Test
    public void zero_bits_count() {
        int value1 = toInt("000");
        assertEquals(0, countBits(value1));
    }


    @Test
    public void multiple_bits_count() {
        int value1 = toInt("101");
        assertEquals(2, countBits(value1));
    }

    @Test
    public void all_bits_on_count() {
        int value1 = toInt("11111111");
        assertEquals(8, countBits(value1));
    }

    @Test
    public void evenParityTest() {
        assertEquals("11010001", toBinary(evenParity((byte) toInt("1010001"))));
        assertEquals("00000000", toBinary(evenParity((byte) toInt("0000000"))));
        assertEquals("01101001", toBinary(evenParity((byte) toInt("1101001"))));
        assertEquals("11111111", toBinary(evenParity((byte) toInt("1111111"))));
    }


    @Test
    public void oddParityTest() {
        assertEquals("10000000", toBinary(oddParity((byte) toInt("0000000"))));
        assertEquals("01010001", toBinary(oddParity((byte) toInt("1010001"))));
        assertEquals("11101001", toBinary(oddParity((byte) toInt("1101001"))));
        assertEquals("01111111", toBinary(oddParity((byte) toInt("1111111"))));
    }

    @Test
    public void setBitOn() {
        byte lock = toByte("00000000");
        assertEquals("00000001", toBinary((byte) set(lock, 1)));
        assertEquals("00000010", toBinary((byte) set(lock, 2)));
        assertEquals("01000000", toBinary((byte) set(lock, 7)));
    }

    @Test
    public void clearBits() {
        byte lock = toByte("10100100");
        assertEquals("10100000", toBinary((byte) clear(lock, 3)));
        assertEquals("10000100", toBinary((byte) clear(lock, 6)));
        assertEquals("00100100", toBinary((byte) clear(lock, 8)));
    }


    @Test
    public void toggleBits() {
        byte lock = toByte("10100100");
        assertEquals("10100101", toBinary((byte) toggle(lock, 1)));
        assertEquals("10000100", toBinary((byte) toggle(lock, 6)));
        assertEquals("00100100", toBinary((byte) toggle(lock, 8)));
    }

}