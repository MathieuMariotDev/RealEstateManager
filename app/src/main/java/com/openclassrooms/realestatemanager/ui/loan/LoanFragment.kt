package com.openclassrooms.realestatemanager.ui.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateViewModel
import com.openclassrooms.realestatemanager.ui.details.DetailsViewModel
import com.openclassrooms.realestatemanager.utils.Constants.CODE_DOLLAR
import com.openclassrooms.realestatemanager.utils.Constants.CODE_EURO
import com.openclassrooms.realestatemanager.utils.LoanSimUtils
import com.openclassrooms.realestatemanager.utils.TextFieldUtils
import com.openclassrooms.realestatemanager.utils.TextFieldUtils.Companion.hasText
import com.openclassrooms.realestatemanager.utils.Utils
import kotlin.math.pow

class LoanFragment : Fragment() {

    lateinit var loanBinding: FragmentLoanBinding
    var durationYear: Int = 1
    private var currencyCode = 0
    private var monthlyPayment: Double? = null
    private var monthlyPaymentInsurance: Double? = null
    private var totalLoan: Double? = null

    private val viewModelLoan: LoanViewModel by activityViewModels() {
        RealEstateViewModelFactory(
            (requireActivity().application as RealEstateApplication).realEstateRepository,
            photoRepository = (requireActivity().application as RealEstateApplication).photoRepository,
            (requireActivity().application as RealEstateApplication).geocoderRepository
        )
    }

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
        observeCurrency()
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
                monthlyPayment =
                    LoanSimUtils.calculateMonthlyPayment(amount, interestRate, durationYear)
                monthlyPaymentInsurance =
                    LoanSimUtils.calculateMonthlyInsurance(insuranceRate, amount)
                totalLoan = LoanSimUtils.totalLoan(
                    monthlyPayment!!,
                    monthlyPaymentInsurance!!,
                    durationYear
                )
                currencySwitchAndDisplay()
            }
        }
    }

    private fun currencySwitchAndDisplay() {
        when (currencyCode) {
            CODE_DOLLAR -> {
                if (monthlyPayment != null && monthlyPaymentInsurance != null) {
                    loanBinding.textviewMonthlyPayment.text = monthlyPayment!!.toInt().toString()
                    loanBinding.currency1.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_dollar_black_24dp
                        )
                    )
                    loanBinding.textviewTotalLoan.text = totalLoan!!.toInt().toString()
                    loanBinding.currency3.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_dollar_black_24dp
                        )
                    )
                }
                if (monthlyPaymentInsurance != null) {
                    loanBinding.textviewMonthlyInsurance.text =
                        monthlyPaymentInsurance!!.toInt().toString()
                    loanBinding.currency2.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_dollar_black_24dp
                        )
                    )
                }
            }
            CODE_EURO -> {
                if (monthlyPayment != null && totalLoan != null) {
                    loanBinding.textviewMonthlyPayment.text =
                        Utils.convertDollarToEuro(monthlyPayment!!.toInt()).toString()
                    loanBinding.currency1.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_euro_black_24dp
                        )
                    )
                    loanBinding.textviewTotalLoan.text =
                        Utils.convertDollarToEuro(totalLoan!!.toInt()).toString()
                    loanBinding.currency3.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_euro_black_24dp
                        )
                    )
                }
                if (monthlyPaymentInsurance != null) {
                    loanBinding.textviewMonthlyInsurance.text =
                        Utils.convertDollarToEuro(monthlyPaymentInsurance!!.toInt()).toString()
                    loanBinding.currency2.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_currency_euro_black_24dp
                        )
                    )
                }
            }
        }
    }

    private fun observeCurrency() {
        viewModelLoan.liveDataCurrencyCode.observe(viewLifecycleOwner, Observer {
            currencyCode = it
            currencySwitchAndDisplay()
        })
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

    private fun validation(): Boolean {
        var check = true
        if (!hasText(
                loanBinding.textFieldAmout,
                "This field must be completed"
            ) || !TextFieldUtils.isNumber(
                loanBinding.textFieldAmout,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(
                loanBinding.textFieldInsuranceRate,
                "This field must be completed"
            ) || !TextFieldUtils.isNumber(
                loanBinding.textFieldInsuranceRate,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(
                loanBinding.textFieldInterestRate,
                "This field must be completed"
            ) || !TextFieldUtils.isNumber(
                loanBinding.textFieldInterestRate,
                "This field must only contain numbers"
            )
        ) check = false
        if (!TextFieldUtils.isNumber(
                loanBinding.textFieldContribution,
                "This field must only contain numbers"
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