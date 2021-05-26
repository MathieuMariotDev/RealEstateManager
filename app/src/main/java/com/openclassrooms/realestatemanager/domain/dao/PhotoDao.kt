package com.openclassrooms.realestatemanager.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import com.openclassrooms.realestatemanager.domain.models.Photo

@Dao
interface PhotoDao {

    @Insert
    suspend fun insert(photo: Photo) :Long

}