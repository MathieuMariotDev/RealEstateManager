package com.openclassrooms.realestatemanager.domain.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class RealEstateRepository(private val realEstateDao: RealEstateDao) {


    private var floxRealEstateWithPhoto : Flow<List<RealEstateWithPhoto>> = realEstateDao.getRealEstateWithPhoto()

    private var flowListRealEstate = realEstateDao.getRealEstate()

    private val liveDataIdRealEstate: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    fun getFlowListRealEstate() = flowListRealEstate


    fun setIdRealEstate(id : Long) {
        liveDataIdRealEstate.value = id
    }

    fun getIdRealEstate() = liveDataIdRealEstate

    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.updateRealEstate(realEstate)

    @WorkerThread
    suspend fun insertRealEstate(realEstate: RealEstate) = withContext(Dispatchers.IO){  //Used for take the value return(id autogenrate) by the insert method
        realEstateDao.insert(realEstate)
    }

    fun getRealEstateWithId(id : Long) : Flow<RealEstateWithPhoto>{
        return realEstateDao.getRealEstateWithId(id)
    }

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
            queryString = "SELECT * FROM real_estate_table r LEFT JOIN PHOTO_TABLE p ON r.id_realestate=p.id_property WHERE 1=1"
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
            if (sold == false) {
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


