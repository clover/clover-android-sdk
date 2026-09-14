package com.clover.sdk.util

import com.clover.sdk.v3.inventory.TaxRate

object TaxRateLabels {
    const val STANDARD = "com.clover.tax.rate.standard"
    const val REDUCED = "com.clover.tax.rate.reduced"

    @JvmStatic
    fun isStandard(taxRate: TaxRate): Boolean = taxRate.systemTaxRate?.labelKey == STANDARD

    @JvmStatic
    fun isReduced(taxRate: TaxRate): Boolean = taxRate.systemTaxRate?.labelKey == REDUCED
}
