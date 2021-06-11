package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateRequest
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto

class
RealEstateViewModel(
    private val realEstateRepository: RealEstateRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {


    private val liveDataRealEstateRequest = MutableLiveData<RealEstateRequest>(RealEstateRequest())

    val liveDataIdRealEstate : LiveData<Long> = realEstateRepository.getIdRealEstate()


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



    //var listRealEstateWithPhoto = realEstateRepository.customQueryOrGetSimpleFlow().asLiveData()
    var realEstate = MutableLiveData<RealEstate>()

    var listRealEstateWithPhoto: LiveData<List<RealEstateWithPhoto>> = Transformations.switchMap(liveDataRealEstateRequest){ realEstateRequest ->
        realEstateRepository.customQueryOrGetSimpleFlow(
            realEstateRequest.minSurface,
            realEstateRequest.maxSurface,
            realEstateRequest.minPrice,
            realEstateRequest.maxPrice,
            realEstateRequest.nearbyStore,
            realEstateRequest.nearbyPark,
            realEstateRequest.nearbyRestaurant,
            realEstateRequest.nearbySchool,
            realEstateRequest.minDateInLong,
            realEstateRequest.sold,
            realEstateRequest.city,
            realEstateRequest.nbPhoto).asLiveData()
    }

    fun setMinSurface(minSurface: Float?) {
        liveDataRealEstateRequest.value?.minSurface = minSurface
    }

    fun setMaxSurface(maxSurface: Float?) {
        liveDataRealEstateRequest.value?.maxSurface = maxSurface
    }

    fun setMinPrice(minPrice: Int?) {
        liveDataRealEstateRequest.value?.minPrice = minPrice
    }

    fun setMaxPrice(maxPrice: Int?) {
        liveDataRealEstateRequest.value?.maxPrice = maxPrice
    }

    fun setNearbyStore(nearbyStore: Boolean) {
        if (nearbyStore == false) {
            liveDataRealEstateRequest.value?.nearbyStore = null
        } else {
            liveDataRealEstateRequest.value?.nearbyStore = nearbyStore
        }
    }

    fun setNearbyPark(nearbyPark: Boolean) {
        if (nearbyPark == false) {
            liveDataRealEstateRequest.value?.nearbyPark = null
        } else {
            liveDataRealEstateRequest.value?.nearbyPark = nearbyPark
        }
    }

    fun setNearbySchool(nearbySchool: Boolean) {
        if (nearbySchool == false) {
            liveDataRealEstateRequest.value?.nearbySchool = null
        } else {
            liveDataRealEstateRequest.value?.nearbySchool = nearbySchool
        }
    }

    fun setNearbyRestaurant(nearbyRestaurant: Boolean) {
        if (nearbyRestaurant == false) {
            liveDataRealEstateRequest.value?.nearbyRestaurant = null
        } else {
            liveDataRealEstateRequest.value?.nearbyRestaurant = nearbyRestaurant
        }
    }

    fun setMinDate(date: Long?) {
        liveDataRealEstateRequest.value?.minDateInLong = date
    }

    fun setSold(sold: Boolean?) {
        liveDataRealEstateRequest.value?.sold = sold
    }

    fun setNbPhoto(nbPhoto: Int?) {
        liveDataRealEstateRequest.value?.nbPhoto = nbPhoto
    }

    fun setCityName(cityName: String?) {
        liveDataRealEstateRequest.value?.city = cityName
    }

    fun validationUpdateQuery() {
        liveDataRealEstateRequest.value = liveDataRealEstateRequest.value
    }

}
