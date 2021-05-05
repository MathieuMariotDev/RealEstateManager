package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.launch

class RealEstateViewModel(private val realEstateRepository: RealEstateRepository, private val photoRepository: PhotoRepository) : ViewModel() {

    //val listRealEstates: LiveData<List<RealEstate>> = realEstateRepository.getRealEstates().asLiveData()

    val listRealEstateWithPhoto: LiveData<List<RealEstateWithPhoto>> = realEstateRepository.getRealEstateWithPhotos().asLiveData()
            // ?

    val liveData = MutableLiveData<Long>()

    fun insert() { //TODO SUSPEND
        if (BuildConfig.DEBUG) {
            viewModelScope.launch {
                liveData.value = realEstateRepository.insertRealEstate(realEstateRepository.addMockRealEstate())
                liveData.value?.let {
                    photoRepository.insertPhoto(photoRepository.addMockPhoto(it))
                }
            }
        }
    }

    fun insertPhoto() {

    }

}
