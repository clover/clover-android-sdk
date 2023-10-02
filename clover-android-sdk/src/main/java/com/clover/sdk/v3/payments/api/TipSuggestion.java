package com.clover.sdk.v3.payments.api;

/**
 * A tip suggestion that can be either amount or percentage based
 */
public class TipSuggestion {
      private final Long tipAmount;
      private final Long tipPercentage;
      private final String name;

      private TipSuggestion(String name, Long tipAmount, Long tipPercentage) {
        this.name = name;
        this.tipAmount = tipAmount;
        this.tipPercentage = tipPercentage;
      }

      public com.clover.sdk.v3.merchant.TipSuggestion getV3TipSuggestion() {
        com.clover.sdk.v3.merchant.TipSuggestion tipSuggestion = new com.clover.sdk.v3.merchant.TipSuggestion();
        tipSuggestion.setName(this.name);
        if (this.tipAmount != null) {
          tipSuggestion.setAmount(this.tipAmount);
        } else {
          tipSuggestion.setPercentage(this.tipPercentage);
        }
        return tipSuggestion;
      }

      /**
       * A tip suggestion of a fixed amount. For example 100 for a $1 tip
       * @param name
       * @param tipAmount
       * @return
       */
      public static TipSuggestion Amount(String name, Long tipAmount) {
        return new TipSuggestion(name, tipAmount, null);
      }

      /**
       * A tip suggestion based on percentage of the amount of the payment. For example, 10 for a 10% tip
       * @param name
       * @param tipPercentage
       * @return
       */
      public static TipSuggestion Percentage(String name, Long tipPercentage) {
        return new TipSuggestion(name, null, tipPercentage);
      }
    }