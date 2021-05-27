package com.openclassrooms.realestatemanager.domain.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList


@Dao
interface RealEstateDao {


    @Query("SELECT * FROM real_estate_table")
    fun getAll(): Flow<List<RealEstate>>

    @Query("SELECT * FROM real_estate_table WHERE idRealEstate = :idRealEstate")
    fun getRealEstateWithCursor(vararg idRealEstate: Long) : Cursor


    @Transaction
    @Query("SELECT * FROM real_estate_table")
    fun getRealEstateWithPhoto() : Flow<List<RealEstateWithPhoto>>

    @Transaction
    @Query("SELECT * FROM REAL_ESTATE_TABLE WHERE surface BETWEEN :minSurface AND :maxSurface")
    fun getRealEstateBetweenTwoSurface(minSurface : Float ,maxSurface : Float) : Flow<List<RealEstate>>  // For test


    @RawQuery(observedEntities = [RealEstate::class])
    fun getRealEstateBetweenSurfaceAndPrice(query : SupportSQLiteQuery) : Flow<List<RealEstate>>
    //OR nearby_store IS TRUE OR nearby_park IS TRUE OR nearby_restaurant IS TRUE*/




    fun customQuery(minSurface : Float? ,maxSurface : Float?,minPrice : Int? , maxPrice : Int?,
                    nearbyStore :Boolean?,nearbyPark :Boolean?,nearbyRestaurant :Boolean?,nearbySchool :Boolean?)  : Flow<List<RealEstate>>{
        var queryString = "SELECT * FROM REAL_ESTATE_TABLE"
        if(minSurface != null && maxSurface != null){
            queryString += " WHERE surface BETWEEN $minSurface AND $maxSurface"
        }
        if (minPrice != null && maxPrice != null){
            queryString +=  " AND price BETWEEN $minPrice AND $maxPrice"
        }

        if (nearbyStore !=null){
            queryString += " AND nearby_store = ${convertToInt(nearbyStore)}"
        }
        if (nearbyPark !=null){
            queryString += " AND nearby_park = ${convertToInt(nearbyPark)}"
        }
        if (nearbyRestaurant !=null){
            queryString += " AND nearby_restaurant = ${convertToInt(nearbyRestaurant)}"
        }
        if (nearbySchool !=null){
            queryString += " AND nearby_school = ${convertToInt(nearbySchool)}"
        }
        val query = SimpleSQLiteQuery("$queryString")
        return getRealEstateBetweenSurfaceAndPrice(query)
    }


    fun convertToInt(bool : Boolean) : Int{
        return when(bool){
            true -> 1
            else -> 0
        }
    }

    @Insert
    suspend fun insert  ( realEstate: RealEstate): Long

    @Delete
    suspend fun delete(vararg realEstate: RealEstate)


    @Query("DELETE FROM REAL_ESTATE_TABLE")
    suspend fun AllDelete()

}