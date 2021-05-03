package com.openclassrooms.realestatemanager.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(

        @PrimaryKey(autoGenerate = true)
        val idPhoto : Int=0,

        @ColumnInfo(name = "path")         //Corresponding to the path of the photo
        val path:Int,

        /*      FOREIGNKEY          */
        val idProperty:Int,

        @ColumnInfo(name = "label")
        val label:String)
