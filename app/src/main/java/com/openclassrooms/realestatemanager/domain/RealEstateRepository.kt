package com.openclassrooms.realestatemanager.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.model.RealEstate
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(val database: RealEstateDao) {


    //private var realEstates = MutableLiveData<List<RealEstate>>()
    private var flowRealEstate : Flow<List<RealEstate>> = database.getAll() // NEED modification Mutable ?
    private var listRealEstate :ArrayList<RealEstate> = ArrayList<RealEstate>()

    suspend fun addMockRealEstate() {

        if (BuildConfig.DEBUG) {
            val mock: Mock = Mock()
            val realEstate = RealEstate(
                    type = mock.getRandomPropertyType(),
                    price = mock.getRandomPrice(),
                    surface = mock.getRandomSurface(),
                    nbRooms = mock.getRandomNbRooms(),
                    description = mock.getDefaultDescription(),
                    address = mock.getRandomAddress(),
                    propertyStatus = false,
                    dateEntry = null,
                    dateSale = null,
                    realEstateAgent = null
                    )
            database.insert(realEstate)
            /*listRealEstate.add(realEstate)
            mutableLiveDataRealEstate.value = listRealEstate*/

        }
    }


    fun getRealEstates() = flowRealEstate

}