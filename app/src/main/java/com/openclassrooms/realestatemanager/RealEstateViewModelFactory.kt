package com.openclassrooms.realestatemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import java.lang.IllegalArgumentException

class RealEstateViewModelFactory(private val realEstateRepository: RealEstateRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RealEstateViewModel(realEstateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}