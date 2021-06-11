package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class FiltersTest {

    lateinit var realEstateDatabase: RealEstateDatabase
    var idRealEstate: Long = 0
    lateinit var liveDataListRealEstate: LiveData<List<RealEstateWithPhoto>>
    lateinit var data: List<RealEstateWithPhoto>
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val date1: Date = GregorianCalendar(2021, Calendar.MAY, 10).time
    val date2: Date = GregorianCalendar(2021, Calendar.MAY, 20).time
    val date3: Date = GregorianCalendar(2021, Calendar.MAY, 30).time

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var realEstateRepo : RealEstateRepository

    fun dateFormat(date: Date): String {
        return dateFormat.format(date)
    }

    @Before
    fun setup() {
        realEstateDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RealEstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        //cleanDataBase()
        insertRealEstate()
        realEstateRepo = RealEstateRepository(realEstateDatabase.RealEstateDao())
    }

    @Test
    fun surfaceFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(750.toFloat(), 1000.toFloat()).asLiveData()
        //assertThat(liveDataListRealEstate.getOrAwaitValue().size, `is`(2))
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.realEstate.surface >= 750)
            assertTrue(realEstate.realEstate.surface <= 1000)
        }
    }


    @Test
    fun PriceFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
                750.toFloat(),
                1000.toFloat(),
                100000,
                150000,
                null,
                null,
                null,
                null,
                null,
                null
            )
            .asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.realEstate.price >= 100000)
            assertTrue(realEstate.realEstate.price <= 150000)
        }
    }


    @Test
    fun NearbyFilter() {
        val nearbyStore = false
        val nearbyPark = false
        val nearbyRestaurant = true
        val nearbySchool = false
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            750.toFloat(),
            1000.toFloat(),
            100000,
            150000,
            nearbyStore,
            nearbyPark,
            nearbyRestaurant,
            nearbySchool,
            null, null
        ).asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.realEstate.nearbyStore == nearbyStore)
            assertTrue(realEstate.realEstate.nearbyPark == nearbyPark)
            assertTrue(realEstate.realEstate.nearbyRestaurant == nearbyRestaurant)
            assertTrue(realEstate.realEstate.nearbySchool == nearbySchool)
        }
    }

    @Test
    fun dateFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            750.toFloat(),
            1000.toFloat(),
            null,
            null,
            null,
            null,
            null,
            null,
            Utils.getTodayDateInLong(dateFormat(date2)),
            null
        ).asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            //assertTrue(realEstate.dateEntry <= Utils.getTodayDateInLong(test(date2)))
            assertTrue(realEstate.realEstate.dateEntry >= Utils.getTodayDateInLong(dateFormat(date1)))
        }
    }

    @Test
    fun soldDateFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            100.toFloat(),
            2000.toFloat(),
            null,
            null,
            null,
            null,
            null,
            null,
            minDateInLong = Utils.getTodayDateInLong(dateFormat(date3)),
            sold = true
        ).asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.realEstate.dateSold != null)
            assertTrue(realEstate.realEstate.dateSold!! >= Utils.getTodayDateInLong(dateFormat(date1)))
        }
    }

    @Test
    fun regionFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            minDateInLong = null,
            sold = null,
            "Long Island"
        ).asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.realEstate.address.contains("Long Island"))
        }
    }

    @Test
    fun nbPhotoFilter() {
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            minDateInLong = null,
            sold = null,
            null,
            3
        ).asLiveData()
        assertTrue(liveDataListRealEstate.getOrAwaitValue().isNotEmpty())
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for (realEstate in data) {
            assertTrue(realEstate.photos!!.isNotEmpty())
            assertTrue(realEstate.photos?.size!! >= 3)
        }
    }

    @Test
    fun multiFilter(){
        liveDataListRealEstate = realEstateRepo.customQueryOrGetSimpleFlow(
            500.toFloat(),
            1500.toFloat(),
            100000,
            200000,
            null,
            null,
            true,
            null,
            minDateInLong = Utils.getTodayDateInLong(dateFormat(date1)),
            sold = null,
            "Washington",
            3
        ).asLiveData()
        data = liveDataListRealEstate.getOrAwaitValue()
        assertTrue(data.isNotEmpty())
        for(realEstate in data){
            assertTrue(realEstate.photos?.size!! >= 3)
            assertTrue(realEstate.realEstate.address.contains("Washington"))
            assertTrue(realEstate.realEstate.dateEntry >= Utils.getTodayDateInLong(dateFormat(date1)))
            assertTrue(realEstate.realEstate.nearbyRestaurant == true)
            assertTrue(realEstate.realEstate.price >= 100000)
            assertTrue(realEstate.realEstate.price <= 200000)
            assertTrue(realEstate.realEstate.surface >= 750)
            assertTrue(realEstate.realEstate.surface <= 1500)
        }


    }

    fun insertRealEstate() = runBlocking {
        idRealEstate = realEstateDatabase.RealEstateDao().insert(
            realEstate = RealEstate(
                type = "Appartement",
                price = 100000,
                surface = 500.toFloat(),
                nbRooms = 12,
                nbBathrooms = 2,
                nbBedrooms = 3,
                description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
                address = "600 Maryland Ave SW, Long Island, DC 20002, États-Unis",
                propertyStatus = false,
                dateEntry = Utils.getTodayDateInLong(dateFormat(date2)),
                dateSold = Utils.getTodayDateInLong(dateFormat(date3)),
                realEstateAgent = "Math",
                latitude = null,
                longitude = null,
                nearbyStore = true,
                nearbyPark = false,
                nearbyRestaurant = true,
                nearbySchool = true
            )
        )
        repeat(3) {
            realEstateDatabase.PhotoDao().insert(
                photo = Photo(
                    idProperty = idRealEstate,
                    path = getRandomPhoto()
                )
            )
        }

        idRealEstate = realEstateDatabase.RealEstateDao().insert(
            realEstate = RealEstate(
                type = "Loft",
                price = 150000,
                surface = 750.toFloat(),
                nbRooms = 12,
                nbBathrooms = 2,
                nbBedrooms = 3,
                description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
                address = "600 Maryland Ave SW, Washington, DC 20002, États-Unis",
                propertyStatus = false,
                dateEntry = Utils.getTodayDateInLong(dateFormat(date1)),
                dateSold = Utils.getTodayDateInLong(dateFormat(date2)),
                realEstateAgent = "Math",
                latitude = null,
                longitude = null,
                nearbyStore = false,
                nearbyPark = false,
                nearbyRestaurant = true,
                nearbySchool = false
            )
        )
        repeat(3) {
            realEstateDatabase.PhotoDao().insert(
                photo = Photo(
                    idProperty = idRealEstate,
                    path = getRandomPhoto()
                )
            )
        }
        idRealEstate = realEstateDatabase.RealEstateDao().insert(
            realEstate = RealEstate(
                type = "Castel",
                price = 200000,
                surface = 1000.toFloat(),
                nbRooms = 12,
                nbBathrooms = 2,
                nbBedrooms = 3,
                description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
                address = "600 Maryland Ave SW, Long Island, DC 20002, États-Unis",
                propertyStatus = false,
                dateEntry = Utils.getTodayDateInLong(dateFormat(date3)),
                dateSold = null,
                realEstateAgent = "Math",
                latitude = null,
                longitude = null,
                nearbyStore = false,
                nearbyPark = true,
                nearbyRestaurant = false,
                nearbySchool = true
            )
        )
        realEstateDatabase.PhotoDao().insert(
            photo = Photo(
                idProperty = idRealEstate,
                path = getRandomPhoto()
            )
        )

    }


    fun getRandomPhoto(): String {
        val randomPhoto = Random.nextInt(PropertyPhoto.values().size)
        return PropertyPhoto.values()[randomPhoto].photoPath.toString()
    }

    enum class PropertyPhoto(val photoPath: Int) {
        Photo1(R.drawable.house1),
        Photo2(R.drawable.house2),
        Photo3(R.drawable.house3)
    }

    fun cleanDataBase() = runBlocking {
        realEstateDatabase.RealEstateDao().allDelete()
    }

}