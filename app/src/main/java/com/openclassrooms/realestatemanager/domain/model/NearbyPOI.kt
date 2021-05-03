package com.openclassrooms.realestatemanager.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "nearby_poi_table")
data class NearbyPOI(

        @PrimaryKey(autoGenerate = false)
        val placeId:Int,

        /*      FOREIGNKEY          */
        val realEstateId:Int,

        @ColumnInfo(name = "latitude")
        val latitude:Int?,

        @ColumnInfo(name = "longitude")
        val longitude:Int?,

        @ColumnInfo(name = "type_poi")
        val typePOI:String)
