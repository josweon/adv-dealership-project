package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesContract extends Contract {
    final private BigDecimal salesTaxAmount = new BigDecimal("0.05");
    final private BigDecimal recordingFee = new BigDecimal("100");
    private BigDecimal processingFee;
    private Boolean toFinance = false;

    public SalesContract() {
        super();
    }

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);

        BigDecimal underPrice = BigDecimal.valueOf(10_000);
        BigDecimal vehiclePrice = vehicleSold.getPrice();

        if (vehiclePrice.compareTo(underPrice) < 0) {
            this.processingFee = new BigDecimal("295");
        } else {
            this.processingFee = new BigDecimal("495");
        }

        this.toFinance = false;
    }

    public Boolean getToFinance() {
        return toFinance;
    }

    public BigDecimal getRecordingFee() {
        return recordingFee;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public BigDecimal getSalesTaxAmount() {
        return this.salesTaxAmount.multiply(getVehicleSold().getPrice()).setScale(2, RoundingMode.HALF_UP);
    }

    public void setToFinance(Boolean toFinance) {
        this.toFinance = toFinance;
    }

    @Override
    public BigDecimal getTotalPrice() {
        BigDecimal vehiclePrice = getVehicleSold().getPrice();
        BigDecimal vehicleSalesTax = vehiclePrice.multiply(this.salesTaxAmount);
        BigDecimal salesTax = vehiclePrice.add(vehicleSalesTax);

        BigDecimal totalPrice = salesTax.add(this.processingFee).add(this.recordingFee);

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        // P * r / (1 - (1 + r)^-n)
        if (!this.toFinance) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {

            BigDecimal vehiclePrice = getVehicleSold().getPrice();
            BigDecimal totalPrice = this.getTotalPrice();
            BigDecimal priceBoundary = BigDecimal.valueOf(10_000);

            BigDecimal monthlyPayment = BigDecimal.ZERO;
            if (vehiclePrice.compareTo(priceBoundary) >= 0) {
                BigDecimal rate = new BigDecimal("0.0425");
                monthlyPayment = this.interestCalculation(totalPrice, rate, 48);

            } else {
                BigDecimal rate = new BigDecimal("0.0525");
                monthlyPayment = this.interestCalculation(totalPrice, rate, 24);
            }

            return monthlyPayment;
        }
    }

    private BigDecimal interestCalculation(BigDecimal price, BigDecimal rate, int durationInMonths) {
        // P * r / (1 - (1 + r)^-n)
        // P * r
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = price.multiply(monthlyRate);

        // (1 - (1 + r)^-n)
        BigDecimal growth = BigDecimal.ONE.add(monthlyRate).pow(durationInMonths);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(growth, 10, RoundingMode.HALF_UP));

        BigDecimal result = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
