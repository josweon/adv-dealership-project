package com.pluralsight;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class LeaseContractTest {

    @Test
    void test_getTotalPrice() {
        Vehicle testV = new Vehicle(
                12345,
                2022,
                "Ford",
                "Grand Master",
                "van",
                "black",
                223532,
                BigDecimal.valueOf(20_000.00));

        LeaseContract testLease = new LeaseContract("2026-11-24", "Richard Pat", "pat@google.com", testV);

        int expected = 1;
        int actual = testLease.getTotalPrice().compareTo(BigDecimal.valueOf(10_000));

        assertEquals(expected, actual);
    }

    @Test
    void test_getMonthlyPayment() {
        Vehicle testV = new Vehicle(
                12345,
                2022,
                "Ford",
                "Grand Master",
                "van",
                "black",
                223532,
                BigDecimal.valueOf(20_000.00));

        LeaseContract testLease = new LeaseContract("2026-11-24", "Richard Pat", "pat@google.com", testV);

        int actual = testLease.getMonthlyPayment().compareTo(testLease.getTotalPrice().divide(BigDecimal.valueOf(36), RoundingMode.HALF_UP));
        int expected = 1;

        assertEquals(expected, actual);
    }
}