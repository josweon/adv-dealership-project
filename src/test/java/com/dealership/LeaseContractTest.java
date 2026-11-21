package com.dealership;

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
    //EXPECTED_ENDING_VALUE|LEASE_FEE|TOTAL_PRICE|MONTHLY_PAYMENT
    //LEASE|20210928|Zachary Westly|zach@texas.com|37846|2021|
    //Chevrolet|Silverado|truck|Black|2750|31995.00|15997.50|2239.65|18237.15|538.43

    //CONTRACT_TYPE|DATE|CUSTOMER_NAME|CUSTOMER_EMAIL|VIN|YEAR|MAKE|MOD
    //EL|VEHICLE_TYPE|COLOR|ODOMETER|VEHICLE_PRICE|[contract-specific-fields]

    @Test
    void test_TotalPriceWithSpecValues() {
        Vehicle testVehicle = new Vehicle(
                37846,
                2021,
                "Chevrolet",
                "Silverado",
                "truck",
                "Black",
                2750,
                new BigDecimal("31995.00"));

        //Total Price = 18237.15
        //Monthly Price = 538.43

        LeaseContract testContract = new LeaseContract("2021-09-28", "Zachary Westly", "zach@texas.com", testVehicle);

        BigDecimal expected = new BigDecimal("18237.15");
        BigDecimal actual = testContract.getTotalPrice();

        assertEquals(expected, actual);
    }

    @Test
    void test_MonthlyPriceWithSpecValues() {
        Vehicle testVehicle = new Vehicle(
                37846,
                2021,
                "Chevrolet",
                "Silverado",
                "truck",
                "Black",
                2750,
                new BigDecimal("31995.00"));

        //Total Price = 18237.15
        //Monthly Price = 538.43

        LeaseContract testContract = new LeaseContract("2021-09-28", "Zachary Westly", "zach@texas.com", testVehicle);

        BigDecimal expected = new BigDecimal("538.43");
        BigDecimal actual = testContract.getMonthlyPayment();

        assertEquals(expected, actual);
    }
}
