package com.openclassrooms.realestatemanager.domain.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithNerbyPOI
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateDao {

    @Query("SELECT * FROM real_estate_table")
    fun getAll(): Flow<List<RealEstate>>

    @Transaction
    @Query("SELECT * FROM real_estate_table")
    fun getRealEstateWithNearbyPOI() : List<RealEstateWithNerbyPOI>


    @Transaction
    @Query("SELECT * FROM real_estate_table")
    fun getRealEstateWithPhoto() : Flow<List<RealEstateWithPhoto>>

    @Insert
    suspend fun insert  ( realEstate: RealEstate): Long

    @Delete
    suspend fun delete(vararg realEstate: RealEstate)

}