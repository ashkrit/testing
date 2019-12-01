package math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FractionsAddTest {


    @Test
    public void adds_zero_value() {

        Fraction result = new Fraction(0).plus(new Fraction(0));
        assertEquals(0, result.intValue());
    }

    @Test
    public void add_single_zero_value() {
        Fraction result = new Fraction(2).plus(new Fraction(0));
        assertEquals(2, result.intValue());
    }


}
