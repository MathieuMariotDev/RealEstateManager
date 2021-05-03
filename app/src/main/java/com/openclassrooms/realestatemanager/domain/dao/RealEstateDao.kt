package com.openclassrooms.realestatemanager.domain.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.domain.model.RealEstate
import com.openclassrooms.realestatemanager.domain.model.RealEstateWithNerbyPOI
import com.openclassrooms.realestatemanager.domain.model.RealEstateWithPhoto
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateDao {

    @Query("SELECT * FROM real_estate_table")
    fun getAll(): Flow<List<RealEstate>>

    @Transaction
    @Query("SELECT * FROM real_estate_table")
    suspend fun getRealEstateWithNearbyPOI() : List<RealEstateWithNerbyPOI>


    @Transaction
    @Query("SELECT * FROM real_estate_table")
    suspend fun getRealEstateWithPhoto() : List<RealEstateWithPhoto>

    @Insert
    suspend fun insert(vararg realEstate: RealEstate)

    @Delete
    suspend fun delete(vararg realEstate: RealEstate)

}