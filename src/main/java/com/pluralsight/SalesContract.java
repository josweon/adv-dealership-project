package com.pluralsight;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesContract extends Contract{
    final private BigDecimal salesTaxAmount = new BigDecimal("0.05");
    final private BigDecimal recordingFee = new BigDecimal("100");
    private BigDecimal processingFee;
    private Boolean toFinance = false;
    private BigDecimal monthlyPayment;
    private BigDecimal totalPrice;

    public SalesContract(){
        super();
    }

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);

        BigDecimal underPrice = BigDecimal.valueOf(10_000);
        BigDecimal vehiclePrice = vehicleSold.getPrice();

        if (vehiclePrice.compareTo(underPrice) <= 0) {
            this.processingFee = new BigDecimal("295");
        } else {
            this.processingFee = new BigDecimal("495");
        }

        this.toFinance = false;
    }

    public Boolean getToFinance() {
        return toFinance;
    }

    public void setToFinance(Boolean toFinance) {
        this.toFinance = toFinance;
    }

    @Override
    public BigDecimal getTotalPrice() {
        BigDecimal vehiclePrice = getVehicleSold().getPrice();
        BigDecimal vehicleSalesTax = vehiclePrice.multiply(this.salesTaxAmount);
        BigDecimal salesTax = vehiclePrice.add(vehicleSalesTax);

        BigDecimal totalPriceWithFees = salesTax.add(this.processingFee).add(this.recordingFee);

        return totalPriceWithFees;
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        if (!this.toFinance) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal vehiclePrice = getVehicleSold().getPrice();
            BigDecimal priceBoundary = BigDecimal.valueOf(10_000);

            BigDecimal monthlyPayment = BigDecimal.ZERO;
            if (vehiclePrice.compareTo(priceBoundary) >= 0) {
                BigDecimal interestRate = new BigDecimal("1.0425");
                BigDecimal duration = BigDecimal.valueOf(48);
                BigDecimal finalPrice = this.getTotalPrice().multiply(interestRate);
                monthlyPayment = finalPrice.divide(duration, RoundingMode.HALF_UP);

            } else {
                BigDecimal interestRate = new BigDecimal("1.0525");
                BigDecimal duration = BigDecimal.valueOf(24);
                BigDecimal finalPrice = this.getTotalPrice().multiply(interestRate);
                monthlyPayment = finalPrice.divide(duration, RoundingMode.HALF_UP);
            }
            return monthlyPayment;
        }
    }
}
