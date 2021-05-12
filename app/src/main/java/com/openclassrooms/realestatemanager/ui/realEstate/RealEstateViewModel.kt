package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.launch

class RealEstateViewModel(private val realEstateRepository: RealEstateRepository, private val photoRepository: PhotoRepository) : ViewModel() {

    val listRealEstateWithPhoto: LiveData<List<RealEstateWithPhoto>> = realEstateRepository.getRealEstateWithPhotos().asLiveData()


}
