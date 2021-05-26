package com.openclassrooms.realestatemanager.domain.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.flow.Flow


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

/*
    @Transaction
    @Query()
    fun getRealEstateBetweenSurfaceAndPrice() : Flow<List<RealEstate>>
    //OR nearby_store IS TRUE OR nearby_park IS TRUE OR nearby_restaurant IS TRUE*/



    fun customQuery(minSurface : Float? ,maxSurface : Float?,minPrice : Int? , maxPrice : Int?) {

    }

    @Insert
    suspend fun insert  ( realEstate: RealEstate): Long

    @Delete
    suspend fun delete(vararg realEstate: RealEstate)


    @Query("DELETE FROM REAL_ESTATE_TABLE")
    suspend fun AllDelete()

}