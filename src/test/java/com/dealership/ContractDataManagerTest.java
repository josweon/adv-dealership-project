package com.dealership;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.dealership.ContractDataManager.saveContract;
import static org.junit.jupiter.api.AssertionsKt.*;

class ContractDataManagerTest {


    @Test
    void testSaveContractSales() {
        Vehicle testV = new Vehicle(12345, 2022,"Ford", "Grand Master", "van", "black", 223532, BigDecimal.valueOf(20_000.00));
        SalesContract contract = new SalesContract("Monday", "Dario Amodei", "dario.com", testV);

        String actual = saveContract(contract);

        assertNotNull(actual);
    }

    @Test
    void testSaveContractLease() {
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

        String actual = saveContract(testContract);

        assertNotNull(actual);
    }
}