package com.pluralsight;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LeaseContract extends Contract{
    final private BigDecimal endingValue;
    final private BigDecimal leaseFee;


    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        this.endingValue = vehicleSold.getPrice().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

        BigDecimal leaseFeePercentage = new BigDecimal("0.07");
        this.leaseFee = vehicleSold.getPrice().multiply(leaseFeePercentage);
    }

    @Override
    public BigDecimal getTotalPrice() {
        return endingValue.add(leaseFee);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        BigDecimal leasePrice = this.getTotalPrice();
        if (leasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal finalLeasePrice = leasePrice.multiply(new BigDecimal("1.04"));
            BigDecimal monthlyPayment = finalLeasePrice.divide(BigDecimal.valueOf(36), RoundingMode.HALF_UP);

            return monthlyPayment;
        }
    }
}