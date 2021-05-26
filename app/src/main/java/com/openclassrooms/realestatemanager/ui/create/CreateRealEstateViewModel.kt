package com.openclassrooms.realestatemanager.ui.create

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.PlacesSearchResult
import com.openclassrooms.realestatemanager.domain.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateRealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
    private val geocoderRepository: GeocoderRepository,
) : ViewModel() {


    val liveData: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val liveDataAddress by lazy {
        MutableLiveData<List<Address>>()
    }

    val liveDataNearbyPOI by lazy {
        MutableLiveData<NearbyPOI>()
    }

    val liveDataValidation by lazy{
        MutableLiveData<Long>()
    }

    val liveDataListPhoto by lazy {
        MutableLiveData<List<Photo>>()
    }

    lateinit var PlacesSearchResult: Array<PlacesSearchResult>

    val listPlaceType = listOf<com.google.maps.model.PlaceType>(
        com.google.maps.model.PlaceType.RESTAURANT,
        com.google.maps.model.PlaceType.PARK,
        com.google.maps.model.PlaceType.SCHOOL,
        com.google.maps.model.PlaceType.STORE
    )

    fun insertRealEstate(realEstate: RealEstate?) {
        viewModelScope.launch {
            liveData.value =
                realEstateRepository.insertRealEstate(realEstate!!)
        }
    }


    fun insertPhoto(photo: Photo) {
        viewModelScope.launch {
            liveDataValidation.value = photoRepository.insertPhoto(photo)
        }

    }
/*
    fun insertMockRealEstate() {
        viewModelScope.launch {
            liveData.value =
                realEstateRepository.insertRealEstate(realEstateRepository.addMockRealEstate())
        }
    }*/
/*
    fun insertMockPhoto() {
        viewModelScope.launch {
            photoRepository.insertPhoto(photoRepository.addMockPhoto(liveData.value!!))
        }
    }*/

    fun getLatLng(textAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataAddress.postValue(geocoderRepository.getLatLng(textAddress))
        }
    }

    fun getNearbyPoi(location: LatLng) {
        //viewModelScope.launch(Dispatchers.IO) {
        val nearbyPoi = NearbyPOI()
        for (type in listPlaceType) {
            PlacesSearchResult =
                (geocoderRepository.getNearbyPoi(location = location, type).results)
            for (placeSearchResult in PlacesSearchResult) {
                for (placeType in placeSearchResult.types) {
                    when (placeType) {
                        PlaceType.RESTAURANT.toString() -> nearbyPoi.nearbyRestaurant = true
                        PlaceType.PARK.toString() -> nearbyPoi.nearbyPark = true
                        PlaceType.STORE.toString() -> nearbyPoi.nearbyStore = true
                        PlaceType.SCHOOL.toString() -> nearbyPoi.nearbySchool = true
                        PlaceType.PRIMARY_SCHOOL.toString() -> nearbyPoi.nearbySchool = true
                        PlaceType.SECONDARY_SCHOOL.toString() -> nearbyPoi.nearbySchool = true
                    }
                }
            }
        }
        liveDataNearbyPOI.postValue(nearbyPoi)
        //  }
    }


    data class NearbyPOI(
        var nearbySchool: Boolean = false,
        var nearbyRestaurant: Boolean = false,
        var nearbyPark: Boolean = false,
        var nearbyStore: Boolean = false
    )

    fun setPhoto(listPhoto: List<Photo>){
        liveDataListPhoto.value = listPhoto
    }

}