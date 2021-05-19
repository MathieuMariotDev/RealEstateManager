package com.openclassrooms.realestatemanager.ui.create

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    val liveData: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val liveDataAddress by lazy {
        MutableLiveData<List<Address>>()
    }

    private lateinit var geocoder : Geocoder

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

    fun updateWithLatLng(context : Context,textAddress : String){
        viewModelScope.launch(Dispatchers.IO) {
            geocoder = Geocoder(context.applicationContext)
            liveDataAddress.postValue(geocoder.getFromLocationName(textAddress,1))
        }
    }


}