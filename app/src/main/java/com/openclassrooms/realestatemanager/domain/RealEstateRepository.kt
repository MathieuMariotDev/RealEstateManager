package com.openclassrooms.realestatemanager.domain

import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.utils.debug.Mock

class RealEstateRepository {

    //private var realEstates = MutableLiveData<List<RealEstate>>()
    private var mutableLiveDataRealEstate = MutableLiveData<List<RealEstate>>()  // NEED modification
    private var listRealEstate :ArrayList<RealEstate> = ArrayList<RealEstate>()
    fun addMockRealEstate() {

        if (BuildConfig.DEBUG) {
            val mock: Mock = Mock()
            val realEstate = RealEstate(
                    type = mock.getRandomPropertyType(),
                    price = mock.getRandomPrice(),
                    surface = mock.getRandomSurface(),
                    nbRooms = mock.getRandomNbRooms(),
                    description = mock.getDefaultDescription(),
                    listPhoto = listOf(mock.getRandomPhoto()),
                    address = mock.getRandomAddress(),
                    nearbyPOI = null,
                    propertyStatus = false,
                    dateEntry = null,
                    dateSale = null,
                    null)
            //realEstates.postValue(realEstate)
            listRealEstate.add(realEstate)
            mutableLiveDataRealEstate.value = listRealEstate
        }

    }


    fun getRealEstates() = mutableLiveDataRealEstate

}