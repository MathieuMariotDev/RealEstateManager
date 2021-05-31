package com.openclassrooms.realestatemanager.domain.dao

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateDao {


    @Query("SELECT * FROM real_estate_table")
    fun getAll(): Flow<List<RealEstate>>

    @Query("SELECT * FROM real_estate_table WHERE id_realestate = :idRealEstate")
    fun getRealEstateWithCursor(vararg idRealEstate: Long): Cursor


    @Transaction
    @Query("SELECT * FROM real_estate_table")
    fun getRealEstateWithPhoto(): Flow<List<RealEstateWithPhoto>>

    @Transaction
    @Query("SELECT * FROM REAL_ESTATE_TABLE WHERE surface BETWEEN :minSurface AND :maxSurface")
    fun getRealEstateBetweenTwoSurface(
        minSurface: Float,
        maxSurface: Float
    ): Flow<List<RealEstateWithPhoto>>  // For test


    @RawQuery(observedEntities = [RealEstate::class])
    fun getRealEstateBetweenSurfaceAndPrice(query: SupportSQLiteQuery): Flow<List<RealEstateWithPhoto>>

    fun customQuery(
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
        var whereOrAnd: Boolean = true
        var queryString : String
        if (nbPhoto != null){
            queryString = "SELECT *, COUNT(p.id_property) FROM real_estate_table r LEFT JOIN PHOTO_TABLE p ON r.id_realestate=p.id_property GROUP BY r.id_realestate HAVING COUNT(p.id_property) >= $nbPhoto"
            whereOrAnd = false
        }else{
            queryString = "SELECT * FROM REAL_ESTATE_TABLE"
        }

        if (minSurface != null && maxSurface != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " surface BETWEEN $minSurface AND $maxSurface"
        }
        if (minPrice != null && maxPrice != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " price BETWEEN $minPrice AND $maxPrice"
        }

        if (nearbyStore != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " nearby_store = ${convertToInt(nearbyStore)}"
        }
        if (nearbyPark != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " nearby_park = ${convertToInt(nearbyPark)}"
        }
        if (nearbyRestaurant != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " nearby_restaurant = ${convertToInt(nearbyRestaurant)}"
        }
        if (nearbySchool != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " nearby_school = ${convertToInt(nearbySchool)}"
        }
        if (minDateInLong != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            if (sold == null) {
                queryString += " date_entry >= $minDateInLong"
            } else if (sold == true) {
                queryString += " date_sale IS NOT NULL AND date_sale >= $minDateInLong"
            }
        }
        if (city != null) {
            if (whereOrAnd) {
                queryString += " WHERE"
                whereOrAnd = false
            } else {
                queryString += " AND"
            }
            queryString += " address LIKE '%$city%'"
        }

    val query = SimpleSQLiteQuery(queryString)
        return getRealEstateBetweenSurfaceAndPrice(query)
    }


    fun convertToInt(bool: Boolean): Int {
        return when (bool) {
            true -> 1
            else -> 0
        }
    }

    @Insert
    suspend fun insert(realEstate: RealEstate): Long

    @Delete
    suspend fun delete(vararg realEstate: RealEstate)


    @Query("DELETE FROM REAL_ESTATE_TABLE")
    suspend fun AllDelete()

}