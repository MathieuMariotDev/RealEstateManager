package com.openclassrooms.realestatemanager.ui.create

import android.location.Address
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Example

import com.openclassrooms.realestatemanager.domain.models.NearbyPOI
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CreateRealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository,
    private val geocoderRepository: GeocoderRepository,
) : ViewModel() {


    val liveData: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val liveDataAddress by lazy {
        MutableLiveData<List<Address>?>()
    }

    val liveDataNearbyPOI by lazy {
        MutableLiveData<NearbyPOI>()
    }

    val liveDataValidation by lazy {
        MutableLiveData<Long>()
    }

    val liveDataListPhoto by lazy {
        MutableLiveData<ArrayList<Photo>>()
    }

    // var PlacesSearchResult: Array<PlacesSearchResult>? = null

    val listPlaceType = listOf<String>(
        "restaurant",
        "park",
        "school",
        "store"
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

    fun getLatLng(textAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataAddress.postValue(geocoderRepository.getLatLng(textAddress))
        }
    }

    fun getNearbyPoi(location: LatLng? = null) {
        viewModelScope.launch() {
            val nearbyPoi = NearbyPOI()
            if (location != null) {
                nearbyPoi.nearbyRestaurant = false
                nearbyPoi.nearbyPark = false
                nearbyPoi.nearbySchool = false
                nearbyPoi.nearbyStore = false
                for (type in listPlaceType) {
                    var reponse: Response<Example>? = withContext(Dispatchers.IO) {
                        geocoderRepository.getNearbyPoi(location = location, type)
                    }
                    for (result in reponse?.body()?.results!!) {
                        for (type in result.types) {
                            when (type) {
                                "restaurant" -> nearbyPoi.nearbyRestaurant =
                                    true
                                "park" -> nearbyPoi.nearbyPark = true
                                "school" -> nearbyPoi.nearbySchool =
                                    true
                                "store" -> nearbyPoi.nearbyStore = true
                            }
                        }
                    }
                }
            }
            liveDataNearbyPOI.postValue(nearbyPoi)
        }
    }

    fun setPhoto(listPhoto: ArrayList<Photo>){
        liveDataListPhoto.value = listPhoto
    }

}