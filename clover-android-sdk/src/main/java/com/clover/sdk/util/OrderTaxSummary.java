package com.clover.sdk.util;

// The purpose of this is to maintain order specific tax information derived from Line
// Item or Payment from Order object.
public class OrderTaxSummary {
    public final String taxName;
    public Long taxRate;
    public Double taxAmount;

    public OrderTaxSummary(String taxName, Long taxRate, Double taxAmount) {
        this.taxName = taxName;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
    }

    // Method to add to existing values
    public void addValues(Double amountToAdd) {
        if (amountToAdd != null) {
            this.taxAmount = (this.taxAmount == null ? 0L : this.taxAmount) + amountToAdd;
        }
    }


    @Override
    public String toString() {
        return "OrderTaxSummary{" +
                "taxName='" + taxName + '\'' +
                ", taxRate=" + taxRate +
                ", taxAmount=" + taxAmount +
                '}';
    }
}