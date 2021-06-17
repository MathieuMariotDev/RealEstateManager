package com.openclassrooms.realestatemanager.ui.update

import android.location.Address
import androidx.lifecycle.*
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResult
import com.openclassrooms.realestatemanager.domain.models.NearbyPOI
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
    private val geocoderRepository: GeocoderRepository
) : ViewModel() {
    val liveDataAddress by lazy {
        MutableLiveData<List<Address>?>()
    }
    val liveDataNearbyPOI by lazy {
        MutableLiveData<NearbyPOI>()
    }
    val liveDataIdRealEstate: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }


    lateinit var liveDataRealEstate: LiveData<RealEstateWithPhoto>

    fun setId(id: Long) {
        liveDataRealEstate = realEstateRepository.getRealEstateWithId(id).asLiveData()
    }

    fun getId() {
        liveDataIdRealEstate.value = realEstateRepository.getIdRealEstate().value
    }


    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch {
            realEstateRepository.updateRealEstate(realEstate)
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.deletePhoto(photo)
        }
    }

    fun insertPhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.insertPhoto(photo)
        }
    }
    fun updatePhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.updatePhoto(photo)
        }
    }
    fun getLatLng(textAddress: String) {
        viewModelScope.launch {
            liveDataAddress.value = geocoderRepository.getLatLng(textAddress)
        }
    }

    fun getNearbyPoi(location: LatLng? = null) {
        //viewModelScope.launch(Dispatchers.IO) {
        val nearbyPoi = NearbyPOI()
        var placesSearchResult: Array<PlacesSearchResult>?
        if(location != null){
            for (type in listPlaceType) {
                placesSearchResult =
                    (geocoderRepository.getNearbyPoi(location = location, type).results)
                if(placesSearchResult !=null){
                    for (placeSearchResult in placesSearchResult!!) {
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
            }
        }
        liveDataNearbyPOI.postValue(nearbyPoi)
    }

    val listPlaceType = listOf<com.google.maps.model.PlaceType>(
        com.google.maps.model.PlaceType.RESTAURANT,
        com.google.maps.model.PlaceType.PARK,
        com.google.maps.model.PlaceType.SCHOOL,
        com.google.maps.model.PlaceType.STORE
    )
}