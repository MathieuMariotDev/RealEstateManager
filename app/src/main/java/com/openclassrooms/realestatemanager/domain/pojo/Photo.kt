package com.openclassrooms.realestatemanager.domain.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = RealEstate::class,
        parentColumns = arrayOf("idRealEstate"),
        childColumns = arrayOf("idProperty"),
onDelete = ForeignKey.CASCADE)],tableName = "photo_table")
data class Photo(

        @PrimaryKey(autoGenerate = true)
        val idPhoto : Long=0,

        @ColumnInfo(name = "path")         //Corresponding to the path of the photo
        val path:Int,

        /*      FOREIGNKEY          */
        @ColumnInfo(index = true)
        val idProperty:Long,

        @ColumnInfo(name = "label")
        val label:String?)
