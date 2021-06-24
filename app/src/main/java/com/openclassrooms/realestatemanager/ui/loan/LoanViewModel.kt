package com.openclassrooms.realestatemanager.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

class LoanViewModel(private val realEstateRepository: RealEstateRepository) : ViewModel() {

    val liveDataCurrencyCode: LiveData<Int> = realEstateRepository.getCurrencyCode()

    fun setCurrencyCode(currencyId: Int) {
        realEstateRepository.setCurrencyCode(currencyId = currencyId)
    }
}
