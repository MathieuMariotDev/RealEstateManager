package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.domain.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RealEstateApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy{
        RealEstateDatabase.getDatabase(this,applicationScope)
    }

    val repository by lazy { RealEstateRepository(database.RealEstateDao()) }



}