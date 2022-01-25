package com.francotte.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoanCalculatorUnitTest {

    private final double loanAmount = 200000;
    private final double interestRate = 2.5;
    private final double loanPeriod = 25 * 12;
    private final double monthlyPayment = 897.23;
    private final double totalPayment = 269170.04;

    @Test
    public void calculateMonthlyPayment_isCorrect() {

        assertEquals(monthlyPayment, Utils.calculateMonthlyPayment(interestRate, loanPeriod, loanAmount), 2);
    }

    @Test
    public void calculateTotalInterests_isCorrect() {

        assertEquals(totalPayment, Utils.calculateTotalPayment(monthlyPayment, loanPeriod), 2);
    }

}

