package com.javaml.computation.shunting_yard;

import org.junit.Assert;
import org.junit.Test;

public class PolishTranslatorTest {
    private PolishTranslator polishTranslator = new PolishTranslator();

    @Test
    public void testTranslate() {
        String actual = polishTranslator.translate("2 + 3 * 4");
        String expected = "2 3 4 * +";
        Assert.assertEquals(expected, actual.trim());
    }
}
