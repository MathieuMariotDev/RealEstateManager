package com.openclassrooms.realestatemanager.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = RealEstate::class,
        parentColumns = arrayOf("id_realestate"),
        childColumns = arrayOf("id_property"),
onDelete = ForeignKey.CASCADE)],tableName = "photo_table")
data class Photo(

        @PrimaryKey(autoGenerate = true)
        val idPhoto : Long=0,

        @ColumnInfo(name = "path")         //Corresponding to the file name of the photo
        var path:String="NoPath",

        /*      FOREIGNKEY          */
        @ColumnInfo(name = "id_property",index = true)
        val idProperty:Long=0,

        @ColumnInfo(name = "label")
        var label:String="NoLabel")
