package com.pluralsight;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LeaseContract extends Contract{
    final private BigDecimal endingValue;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        this.endingValue = vehicleSold.getPrice().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalPrice() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        return BigDecimal.ZERO;
    }
}
