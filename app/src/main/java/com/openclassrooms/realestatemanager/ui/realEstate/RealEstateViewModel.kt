package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto

class RealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    val liveDataMinSurface: MutableLiveData<Float?> by lazy {
        MutableLiveData<Float?>()
    }

    val liveDataMaxSurface: MutableLiveData<Float?> by lazy {
        MutableLiveData<Float?>()
    }
    val liveDataMinPrice: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>()
    }
    val liveDataMaxPrice: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>()
    }
    val liveDataNearbyStore: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    val liveDataNearbyPark: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    val liveDataNearbyRestaurant: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    val liveDataNearbySchool: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    val liveDataMinDateInLong: MutableLiveData<Long?> by lazy {
        MutableLiveData<Long?>()
    }
    val liveDataSold: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    val liveDataCity: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }
    val liveDataNbPhoto: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>()
    }

    //val listRealEstateWithPhoto: LiveData<List<RealEstateWithPhoto>> = realEstateRepository.getRealEstateWithPhotos().asLiveData()

    var listRealEstateWithPhoto = realEstateRepository.customQueryOrGetSimpleFlow().asLiveData()
    var realEstate = MutableLiveData<RealEstate>()

    /*var listRealEstateWithPhoto: LiveData<List<RealEstateWithPhoto>> = Transformations.switchMap(liveDataMinSurface){  ->
        realEstateRepository.customQueryOrGetSimpleFlow().asLiveData()
    }*/

    fun setMinSurface(minSurface: Float?) {
        this.liveDataMinSurface.setValue(minSurface)
    }

    fun setMaxSurface(maxSurface: Float?) {
        this.liveDataMaxSurface.value = maxSurface
    }

    fun setMinPrice(minPrice: Int?) {
        this.liveDataMinPrice.value = minPrice
    }

    fun setMaxPrice(maxPrice: Int?) {
        this.liveDataMaxPrice.value = maxPrice
    }

    fun setNearbyStore(nearbyStore: Boolean) {
        if (nearbyStore == false) {
            this.liveDataNearbyStore.value = null
        } else {
            this.liveDataNearbyStore.value = nearbyStore
        }
    }

    fun setNearbyPark(nearbyPark: Boolean) {
        if (nearbyPark == false) {
            this.liveDataNearbyPark.value = null
        } else {
            this.liveDataNearbyPark.value = nearbyPark
        }
    }

    fun setNearbySchool(nearbySchool: Boolean) {
        if (nearbySchool == false) {
            this.liveDataNearbySchool.value = null
        } else {
            this.liveDataNearbySchool.value = nearbySchool
        }
    }

    fun setNearbyRestaurant(nearbyRestaurant: Boolean) {
        if (nearbyRestaurant == false) {
            this.liveDataNearbyRestaurant.value = null
        } else {
            this.liveDataNearbyRestaurant.value = nearbyRestaurant
        }
    }

    fun setMinDate(date: Long?) {
        this.liveDataMinDateInLong.value = null //TODO
    }

    fun setSold(sold: Boolean?) {
        if (sold == false) {
            this.liveDataSold.value = null
        } else {
            this.liveDataSold.value = sold
        }
    }

    fun setNbPhoto(nbPhoto: Int?) {
        this.liveDataNbPhoto.value = nbPhoto
    }

    fun setCityName(cityName: String?) {
        this.liveDataCity.value = cityName
    }

    fun validationUpdateQuery() {
        listRealEstateWithPhoto = realEstateRepository.customQueryOrGetSimpleFlow(
            minSurface = liveDataMinSurface.value,
            maxSurface = liveDataMaxSurface.value,
            minPrice = liveDataMinPrice.value,
            maxPrice = liveDataMaxPrice.value,
            nearbyStore = liveDataNearbyStore.value,
            nearbyPark = liveDataNearbyPark.value,
            nearbySchool = liveDataNearbySchool.value,
            nearbyRestaurant = liveDataNearbyRestaurant.value,
            minDateInLong = null,
            sold = liveDataSold.value,
            city = liveDataCity.value,
            nbPhoto = liveDataNbPhoto.value
        ).asLiveData()
        
    }

}
