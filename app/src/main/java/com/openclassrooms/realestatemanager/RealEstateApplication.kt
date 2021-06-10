package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.domain.repository.PhotoRepository
import com.openclassrooms.realestatemanager.domain.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RealEstateApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    companion object {
        lateinit var instance : Application
    }



    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val database by lazy{
        RealEstateDatabase.getDatabase(this,applicationScope)
    }

    val geocoderRepository by lazy { GeocoderRepository(context = instance) }

    val realEstateRepository by lazy { RealEstateRepository(database.RealEstateDao()) }

    val photoRepository by lazy { PhotoRepository(database.PhotoDao()) }


}
