package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.ui.loan.LoanFragment
import com.openclassrooms.realestatemanager.utils.LoanSimUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.properties.Delegates

class LoanUnitTest {

    @Test
    fun monthlyPayment() {
        val monthlyPayment = LoanSimUtils.calculateMonthlyPayment(100000, 5.toDouble(), 10)
        assertEquals(1061, monthlyPayment.toFloat().roundToInt())
    }

    @Test
    fun monthlyInsurance() {
        val monthlyPaymentInsurance = LoanSimUtils.calculateMonthlyInsurance(5.toDouble(), 100000)
        assertEquals(417, monthlyPaymentInsurance.roundToInt())
    }

    @Test
    fun totalLoan() {
        val totalLoan = LoanSimUtils.totalLoan(1060.66, 416.67, 10)
        assertEquals(177280, totalLoan.roundToInt())
    }
}