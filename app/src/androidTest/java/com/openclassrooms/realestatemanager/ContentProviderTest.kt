package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.database.DatabaseUtils
import android.util.Log
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.provider.RealEstateContentProvider
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {

    lateinit var contentResolver : ContentResolver
    private var id : Long = 0
    private lateinit var realEstateRepository: RealEstateRepository
    private lateinit var realEstateDatabase: RealEstateDatabase

    @Before
    fun setup(){
        realEstateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,RealEstateDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
        realEstateRepository = RealEstateRepository(realEstateDatabase.RealEstateDao())
    }

    fun insertRealEstate()= runBlocking {
        id = realEstateRepository.insertRealEstate(realEstate = RealEstate(
            type = "Appartement",
            price = 1000999,
            surface = 500.toFloat(),
            nbRooms = 12,
            nbBathrooms = 2,
            nbBedrooms = 3,
            description = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée.",
            address = "600 Maryland Ave SW, Washington, DC 20002, États-Unis",
            propertyStatus = false,
            dateEntry = null,
            dateSale = null,
            realEstateAgent = null,
            latitude = null,
            longitude = null,
            nearbyStore = true,
            nearbyPark = true,
            nearbyRestaurant = false,
            nearbySchool = true
        ))
    }


    @Test
    fun getRealEstateWhenOneItemInserted() {
        insertRealEstate()
        val cursor = contentResolver.query(
            ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, id),
            null,
            null,
            null,
            null
        )
        Log.d(
            "Content Provider",
            "getRealEstateWhenOneItemInserted: " + DatabaseUtils.dumpCursorToString(cursor)
        )
        assertThat(cursor, notNullValue())
        if (cursor != null) {
            assertThat(cursor.count , `is`(1))
            assert(cursor.moveToFirst())
            assert(cursor.getString(cursor.getColumnIndexOrThrow("type")) == "appartement")
            cursor.close()
        }
    }
}