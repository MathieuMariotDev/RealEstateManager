package com.openclassrooms.realestatemanager.domain.pojo

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName = "real_estate_table")
data class RealEstate(
        @PrimaryKey(autoGenerate = true)
        var idRealEstate:Long=0,

        @ColumnInfo(name = "type")
        var type:String,

        @ColumnInfo(name = "price")
        var price:Int,

        @ColumnInfo(name="surface")
        var surface:Float,

        @ColumnInfo(name = "nbRooms")
        var nbRooms:Int,

        @ColumnInfo(name = "description")
        var description:String,

        @ColumnInfo(name = "address")
        var address:String,

        @ColumnInfo(name = "property_status")
        var propertyStatus:Boolean,

        @ColumnInfo(name = "date_entry")
        var dateEntry: Long?,

        @ColumnInfo(name = "date_sale")
        var dateSale: Long?,

        @ColumnInfo(name = "realEstateAgent")
        var realEstateAgent:String?)
