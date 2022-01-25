package com.francotte.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormatDateUnitTest {
    @Test
    public void formatDate_isCorrect() {
        String date = "25/01/2022";
        assertEquals(date, Utils.getTodayDate());
    }
}

