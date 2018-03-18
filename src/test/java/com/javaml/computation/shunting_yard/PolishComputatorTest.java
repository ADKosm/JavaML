package com.javaml.computation.shunting_yard;

import org.junit.Assert;
import org.junit.Test;

public class PolishComputatorTest {
    private PolishComputator polishComputator = new PolishComputator();
    @Test
    public void testCompute() {
        Integer actual = polishComputator.compute("(2 + 3 * 4 - 5) / 3");
        Integer expected = 3;
        Assert.assertEquals(expected, actual);
    }
}
