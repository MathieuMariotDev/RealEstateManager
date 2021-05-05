package com.openclassrooms.realestatemanager.domain

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RealEstateRepository(val realEstateDao: RealEstateDao) {


    private var flowRealEstate : Flow<List<RealEstate>> = realEstateDao.getAll() // NEED modification Mutable ?
    private val mock: Mock = Mock()
    private var floxRealEstateWithPhoto : Flow<List<RealEstateWithPhoto>> = realEstateDao.getRealEstateWithPhoto()


    suspend fun addMockRealEstate() : RealEstate {
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
                    realEstateAgent = null,
                    latitude = null,
                    longitude = null,
                    nearbyStore = mock.getRandomNearbyPOI(),
                    nearbyPark = mock.getRandomNearbyPOI(),
                    nearbyRestaurant = mock.getRandomNearbyPOI(),
                    nearbySchool = mock.getRandomNearbyPOI()
                    )
        return realEstate
    }

    @WorkerThread
    suspend fun insertRealEstate(realEstate: RealEstate) = withContext(Dispatchers.IO){  //Used for take the value return(id autogenrate) by the insert method
        realEstateDao.insert(realEstate)
    }


    fun getRealEstateWithPhotos() = floxRealEstateWithPhoto

    fun getRealEstates() = flowRealEstate

}