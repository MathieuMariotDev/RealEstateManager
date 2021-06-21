package com.openclassrooms.realestatemanager.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.utils.TextFieldUtils
import kotlin.math.pow

class LoanFragment : Fragment() {

    lateinit var loanBinding: FragmentLoanBinding
    var durationYear: Int = 1

    companion object {
        fun newInstance() = LoanFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loanBinding = FragmentLoanBinding.inflate(inflater, container, false)
        onValidation()
        sliderTracking()
        hideShow(false)
        return loanBinding.root
    }

    fun onValidation() {
        loanBinding.buttonValidation.setOnClickListener {
            if (validation()) {
                hideShow(true)
                val contribution =
                    loanBinding.textFieldContribution.editText?.text.toString().toIntOrNull()
                var amount = loanBinding.textFieldAmout.editText?.text.toString().toInt()
                if (contribution != null) {
                    amount -= contribution
                }
                val interestRate =
                    loanBinding.textFieldInterestRate.editText?.text.toString().toDouble()
                val insuranceRate =
                    loanBinding.textFieldInsuranceRate.editText?.text.toString().toDouble()
                val monthlyPayment = calculateMonthlyPayment(amount, interestRate, durationYear)
                val monthlyPaymentInsurance =
                    calculateMonthlyInsurance(insuranceRate, amount, durationYear)
                val totalLoan = totalLoan(monthlyPayment, monthlyPaymentInsurance, durationYear)
                loanBinding.textviewMonthlyPayment.text = monthlyPayment.toInt().toString()
                loanBinding.textviewMonthlyInsurance.text =
                    monthlyPaymentInsurance.toInt().toString()
                loanBinding.textviewTotalLoan.text = totalLoan.toInt().toString()
            }
        }
    }

    private fun calculateMonthlyPayment(
        amount: Int,
        interestRate: Double,
        durationYear: Int
    ): Double {
        val monthlyInterest: Double = (interestRate / 100) / 12
        val durationMonth: Double = (durationYear.toDouble() * 12)
        return (amount * monthlyInterest) / (1 - (1 + monthlyInterest).pow(-durationMonth))
    }

    private fun calculateMonthlyInsurance(
        insuranceRate: Double,
        amount: Int,
        durationYear: Int
    ): Double {
        val insuranceRatePourcent = insuranceRate / 100
        return insuranceRatePourcent * amount / 12
    }

    private fun totalLoan(
        monthlyPayment: Double,
        monthlyPaymentInsurance: Double,
        durationYear: Int
    ): Double {
        val durationMonth: Double = (durationYear.toDouble() * 12)
        return (monthlyPayment + monthlyPaymentInsurance) * durationMonth
    }

    private fun sliderTracking() {
        loanBinding.sliderDuration.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                durationYear = slider.value.toInt()
                loanBinding.textviewDuration.text = durationYear.toString()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                durationYear = slider.value.toInt()
                loanBinding.textviewDuration.text = durationYear.toString()
            }
        })
    }

    fun validation(): Boolean {
        var check = true
        if (!TextFieldUtils.isNumber(
                loanBinding.textFieldAmout,
                "This field must be completed"
            )
        ) check = false
        if (!TextFieldUtils.isNumber(
                loanBinding.textFieldInsuranceRate,
                "This field must be completed"
            )
        ) check = false
        if (!TextFieldUtils.isNumber(
                loanBinding.textFieldInterestRate,
                "This field must be completed"
            )
        ) check = false
        if (!TextFieldUtils.isNumber(
                loanBinding.textFieldContribution,
                "This field must be completed"
            )
        ) check = false
        return check
    }

    fun hideShow(visibilty: Boolean) {
        if (visibilty) {
            loanBinding.cardviewResult.visibility = View.VISIBLE
        } else if (!visibilty) {
            loanBinding.cardviewResult.visibility = View.GONE
        }

    }
}