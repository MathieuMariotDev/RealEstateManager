package com.openclassrooms.realestatemanager.domain

import androidx.annotation.WorkerThread
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class RealEstateRepository(private val realEstateDao: RealEstateDao) {


    private var flowRealEstate : Flow<List<RealEstate>> = realEstateDao.getAll() // NEED modification Mutable ?
    private val mock: Mock = Mock()
    private var floxRealEstateWithPhoto : Flow<List<RealEstateWithPhoto>> = realEstateDao.getRealEstateWithPhoto()

/*
    suspend fun addMockRealEstate() : RealEstate {
            val realEstate = RealEstate(
                    type = mock.getRandomPropertyType(),
                    price = mock.getRandomPrice(),
                    surface = mock.getRandomSurface(),
                    nbRooms = mock.getRandomNbRooms(),
                    nbBathrooms = mock.getRandomNbRoom(),
                    nbBedrooms = mock.getRandomNbRoom(),
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
    }*/

    @WorkerThread
    suspend fun insertRealEstate(realEstate: RealEstate) = withContext(Dispatchers.IO){  //Used for take the value return(id autogenrate) by the insert method
        realEstateDao.insert(realEstate)
    }


    fun getRealEstateWithPhotos() = floxRealEstateWithPhoto

    fun customQueryOrGetSimpleFlow(
        minSurface: Float? = null,
        maxSurface: Float? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        nearbyStore: Boolean? = null,
        nearbyPark: Boolean? = null,
        nearbyRestaurant: Boolean? = null,
        nearbySchool: Boolean? = null,
        minDateInLong: Long? = null,
        sold: Boolean? = null,
        city: String? = null,
        nbPhoto: Int? = null
    ): Flow<List<RealEstateWithPhoto>> {
        var queryString : String
        var queryStringEnd : String? = null
        if (nbPhoto != null){
            queryString = "SELECT *, COUNT(p.id_property) FROM real_estate_table r LEFT JOIN PHOTO_TABLE p ON r.id_realestate=p.id_property WHERE 1=1"
            queryStringEnd = " GROUP BY r.id_realestate HAVING COUNT(p.id_property) >= $nbPhoto"
        }else{
            queryString = "SELECT * FROM REAL_ESTATE_TABLE WHERE 1 = 1"
        }

        if (minSurface != null && maxSurface != null) {
            queryString += " AND surface BETWEEN $minSurface AND $maxSurface"
        }
        if (minPrice != null && maxPrice != null) {
            queryString += " AND price BETWEEN $minPrice AND $maxPrice"
        }

        if (nearbyStore != null) {
            queryString += " AND nearby_store = ${convertToInt(nearbyStore)}"
        }
        if (nearbyPark != null) {
            queryString += " AND nearby_park = ${convertToInt(nearbyPark)}"
        }
        if (nearbyRestaurant != null) {
            queryString += " AND nearby_restaurant = ${convertToInt(nearbyRestaurant)}"
        }
        if (nearbySchool != null) {
            queryString += " AND nearby_school = ${convertToInt(nearbySchool)}"
        }
        if (minDateInLong != null) {
            if (sold == null) {
                queryString += " AND date_entry >= $minDateInLong"
            } else if (sold == true) {
                queryString += " AND date_sale IS NOT NULL AND date_sale >= $minDateInLong"
            }
        }
        if (city != null) {
            queryString += " AND address LIKE '%$city%'"
        }
        if(queryStringEnd != null){
            queryString += queryStringEnd
        }
        val query = SimpleSQLiteQuery((queryString))

        return realEstateDao.getRealEstateWithQuery(query)
    }


    fun convertToInt(bool: Boolean): Int {
        return when (bool) {
            true -> 1
            else -> 0
        }
    }


}


