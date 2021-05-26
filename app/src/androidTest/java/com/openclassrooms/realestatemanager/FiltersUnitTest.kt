package com.openclassrooms.realestatemanager

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.openclassrooms.realestatemanager.domain.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import junit.framework.TestCase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@RunWith(AndroidJUnit4::class)
class FiltersUnitTest {

lateinit var realEstateDatabase : RealEstateDatabase
var idRealEstate : Long = 0
    lateinit var liveDataListRealEstate: LiveData<List<RealEstate>>
    lateinit var data : List<RealEstate>
    lateinit var flowRealEstate :/*Flow<*/List<RealEstate>//>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        realEstateDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RealEstateDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cleanDataBase()
        insertRealEstate()
    }

    @Test
    fun surfaceFilter() {
        liveDataListRealEstate = realEstateDatabase.RealEstateDao().getRealEstateBetweenTwoSurface(750.toFloat(),1000.toFloat()).asLiveData()
        assertThat(liveDataListRealEstate.getOrAwaitValue().size, `is`(2))
        data = liveDataListRealEstate.getOrAwaitValue()
        for (realEstate in data) {
            assertTrue(realEstate.surface >= 750)
            assertTrue(realEstate.surface <= 1000)
        }
    }

    /*
    @Test
    fun surfaceAndPriceFilter(){
        liveDataListRealEstate = realEstateDatabase.RealEstateDao().getRealEstateBetweenSurfaceAndPrice().asLiveData()
        assertThat(liveDataListRealEstate.getOrAwaitValue().size, `is`(1))
    }*/




    fun insertRealEstate()= runBlocking {
        idRealEstate = realEstateDatabase.RealEstateDao().insert(realEstate = RealEstate(
            type = "Appartement",
            price = 100000,
            surface = 500.toFloat(),
            nbRooms = 12,
            nbBathrooms = 2,
            nbBedrooms = 3,
            description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
            address = "600 Maryland Ave SW, Washington, DC 20002, États-Unis",
            propertyStatus = false,
            dateEntry = 1.toLong(),
            dateSale = null,
            realEstateAgent = null,
            latitude = null,
            longitude = null,
            nearbyStore = true,
            nearbyPark = true,
            nearbyRestaurant = false,
            nearbySchool = true
        )
        )
        idRealEstate = realEstateDatabase.RealEstateDao().insert(realEstate = RealEstate(
            type = "Loft",
            price = 150000,
            surface = 750.toFloat(),
            nbRooms = 12,
            nbBathrooms = 2,
            nbBedrooms = 3,
            description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
            address = "600 Maryland Ave SW, Washington, DC 20002, États-Unis",
            propertyStatus = false,
            dateEntry = 1.toLong(),
            dateSale = null,
            realEstateAgent = null,
            latitude = null,
            longitude = null,
            nearbyStore = true,
            nearbyPark = true,
            nearbyRestaurant = false,
            nearbySchool = true
        )
        )
        idRealEstate = realEstateDatabase.RealEstateDao().insert(realEstate = RealEstate(
            type = "Castel",
            price = 200000,
            surface = 1000.toFloat(),
            nbRooms = 12,
            nbBathrooms = 2,
            nbBedrooms = 3,
            description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
            address = "600 Maryland Ave SW, Washington, DC 20002, États-Unis",
            propertyStatus = false,
            dateEntry = 1.toLong(),
            dateSale = null,
            realEstateAgent = null,
            latitude = null,
            longitude = null,
            nearbyStore = true,
            nearbyPark = true,
            nearbyRestaurant = false,
            nearbySchool = true
        )
        )

    }


    fun cleanDataBase()= runBlocking{
        realEstateDatabase.RealEstateDao().AllDelete()
    }

}