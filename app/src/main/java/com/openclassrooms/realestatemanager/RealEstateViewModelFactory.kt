package com.openclassrooms.realestatemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateViewModel
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import java.lang.IllegalArgumentException

class RealEstateViewModelFactory(private val realEstateRepository: RealEstateRepository,private val photoRepository: PhotoRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RealEstateViewModel(realEstateRepository,photoRepository) as T
        }
        else if(modelClass.isAssignableFrom(CreateRealEstateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CreateRealEstateViewModel(realEstateRepository,photoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}