package com.openclassrooms.realestatemanager.domain

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.domain.dao.PhotoDao
import com.openclassrooms.realestatemanager.domain.pojo.Photo
import com.openclassrooms.realestatemanager.utils.debug.Mock

class PhotoRepository(private val photoDao: PhotoDao) {

    private val mock: Mock = Mock()


    @WorkerThread
    suspend fun insertPhoto(photo: Photo) {
        photoDao.insert(photo)
    }


    fun addMockPhoto(idProperty: Long): Photo {
        val photo = Photo(
                path = mock.getRandomPhoto(),
                label = null,
                idProperty = idProperty)
        return photo;
    }
}