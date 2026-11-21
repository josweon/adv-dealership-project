package com.pluralsight;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SalesContractTest {

    @Test
    void test_getToFinance() {
        SalesContract testFinance = new SalesContract();

        boolean expected = false;
        boolean actual = testFinance.getToFinance();

        assertEquals(expected, actual);
    }

    @Test
    void test_getTotalPrice() {
        Vehicle testV = new Vehicle(12345, 2022,"Ford", "Grand Master", "van", "black", 223532, BigDecimal.valueOf(20_000.00));
        SalesContract contract = new SalesContract("Monday", "Dario Amodei", "dario.com", testV);

        int expected = 1;

        BigDecimal basePrice = BigDecimal.valueOf(20_000);
        int actual = contract.getTotalPrice().compareTo(basePrice);

        assertEquals(expected, actual);
    }

    @Test
    void test_getMonthlyPayment() {
        Vehicle testV = new Vehicle(12345, 2022,"Ford", "Grand Master", "van", "black", 223532, BigDecimal.valueOf(20_000.00));
        SalesContract contract = new SalesContract("Monday", "Dario Amodei", "dario.com", testV);

        BigDecimal expected = BigDecimal.ZERO;

        BigDecimal actual = contract.getMonthlyPayment();

        assertEquals(expected, actual);
    }

    @Test
    void test_getMonthlyPayment_Finance() {
        Vehicle testV = new Vehicle(12345, 2022,"Ford", "Grand Master", "van", "black", 223532, BigDecimal.valueOf(20_000.00));
        SalesContract contract = new SalesContract("Monday", "Dario Amodei", "dario.com", testV);

        contract.setToFinance(true);

        int expected = -1;

        int actual = contract.getMonthlyPayment().compareTo(BigDecimal.valueOf(20_000));

        assertEquals(expected, actual);
    }
}