package com.openclassrooms.realestatemanager.ui.update

import android.location.Address
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Example
import com.openclassrooms.realestatemanager.domain.models.NearbyPOI
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

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


    val listPlaceType = listOf<String>(
        "restaurant",
        "park",
        "school",
        "store"
    )
}