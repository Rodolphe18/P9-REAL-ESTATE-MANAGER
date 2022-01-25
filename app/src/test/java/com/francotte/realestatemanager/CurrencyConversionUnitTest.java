package com.francotte.realestatemanager;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrencyConversionUnitTest {

    private final int euro = 100;
    private final int dollar = 119;

    @Test
    public void conversionEuroToDollar_isCorrect() {
        int euro = 100;
        int dollar = 119;
        assertEquals(dollar, Utils.convertEuroToDollars(euro));
    }

    @Test
    public void conversionDollarsToEuro_isCorrect() {
        int euro = 100;
        int dollar = 119;
        assertEquals(euro, Utils.convertDollarToEuro(dollar));
    }
}

