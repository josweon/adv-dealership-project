package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LeaseContract extends Contract{
    final private BigDecimal endingValue;
    final private BigDecimal leaseFee;


    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        this.endingValue = vehicleSold.getPrice().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

        BigDecimal leaseFeePercentage = new BigDecimal("0.07");
        this.leaseFee = vehicleSold.getPrice().multiply(leaseFeePercentage).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getEndingValue() {
        return endingValue;
    }

    public BigDecimal getLeaseFee() {
        return leaseFee;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return endingValue.add(leaseFee).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        BigDecimal leasePrice = this.getTotalPrice();
        if (leasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal monthlyPayment = BigDecimal.ZERO;
            BigDecimal rate = new BigDecimal("0.04");
            int durationInMonths = 36;

//            monthlyPayment = this.getTotalPrice().multiply(new BigDecimal("1.04")).divide(BigDecimal.valueOf(36), RoundingMode.HALF_UP);
            monthlyPayment = interestCalculation(this.getTotalPrice(), rate, durationInMonths);

            return monthlyPayment;
        }
    }

    private BigDecimal interestCalculation(BigDecimal price, BigDecimal rate, int durationInMonths) {
        // P * r / (1 - (1 + r)^-n)
        // P * r
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12),10, RoundingMode.HALF_UP);

        BigDecimal numerator = price.multiply(monthlyRate);

        // (1 - (1 + r)^-n)
        BigDecimal growth = BigDecimal.ONE.add(monthlyRate).pow(durationInMonths);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(growth, 10, RoundingMode.HALF_UP));

        BigDecimal result = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        return result;
    }
}
