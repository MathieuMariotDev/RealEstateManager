package com.openclassrooms.realestatemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateViewModel
import com.openclassrooms.realestatemanager.ui.details.DetailsViewModel
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import com.openclassrooms.realestatemanager.ui.update.UpdateViewModel
import java.lang.IllegalArgumentException

class RealEstateViewModelFactory(private val realEstateRepository: RealEstateRepository, private val photoRepository: PhotoRepository, private val geocoderRepository: GeocoderRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RealEstateViewModel(realEstateRepository,photoRepository) as T
        }
        else if(modelClass.isAssignableFrom(CreateRealEstateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CreateRealEstateViewModel(realEstateRepository,photoRepository,geocoderRepository) as T
        }
        else if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(realEstateRepository) as T
        }
        else if(modelClass.isAssignableFrom(UpdateViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return UpdateViewModel(realEstateRepository,photoRepository,geocoderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}