package com.openclassrooms.realestatemanager.ui.create

import android.location.Address
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
    private val geocoderRepository: GeocoderRepository,
) : ViewModel()  {




    val liveData: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val liveDataAddress by lazy {
        MutableLiveData<List<Address>>()
    }



    fun insertRealEstate(realEstate: RealEstate?) {
        viewModelScope.launch {
            liveData.value =
                realEstateRepository.insertRealEstate(realEstate!!)
        }
    }


    fun insertPhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.insertPhoto(photo)
        }

    }
/*
    fun insertMockRealEstate() {
        viewModelScope.launch {
            liveData.value =
                realEstateRepository.insertRealEstate(realEstateRepository.addMockRealEstate())
        }
    }*/

    fun insertMockPhoto() {
        viewModelScope.launch {
            photoRepository.insertPhoto(photoRepository.addMockPhoto(liveData.value!!))
        }
    }

    fun updateWithLatLng(textAddress : String){
        viewModelScope.launch(Dispatchers.IO) {
            liveDataAddress.postValue(geocoderRepository.getLatLng(textAddress))
        }
    }





}