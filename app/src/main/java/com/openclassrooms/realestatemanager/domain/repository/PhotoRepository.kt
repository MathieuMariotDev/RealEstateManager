
package com.openclassrooms.realestatemanager.domain.repository

import android.app.Application
import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.domain.dao.PhotoDao
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(private val photoDao: PhotoDao) {

    private val mock: Mock = Mock()


    @WorkerThread
    suspend fun insertPhoto(photo: Photo)= withContext(Dispatchers.IO) {
        photoDao.insert(photo)
    }

    suspend fun deletePhoto(photo: Photo){
        photoDao.delete(photo)
    }

}