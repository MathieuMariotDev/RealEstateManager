package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.database.DatabaseUtils
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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
    private lateinit var realEstateDatabase: RealEstateDatabase

    @Before
    fun setup(){
        realEstateDatabase = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RealEstateDatabase::class.java,"real_estate_database")
            .allowMainThreadQueries()
            .build()
        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
        cleanDataBase()
    }


    @Test
    fun getRealEstateWhenNoItemInserted(){
        val cursor = contentResolver.query(
            ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, id),
            null,
            null,
            null,
            null
        )
        assertThat(cursor?.count, `is`(0))

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
        assertThat(cursor, notNullValue())
            Log.d(
                "Content Provider",
                "getRealEstateWhenOneItemInserted: " + DatabaseUtils.dumpCursorToString(cursor)
            )
            assertThat(cursor!!.count , `is`(1))
            assert(cursor.moveToFirst())
            assert(cursor.getString(cursor.getColumnIndexOrThrow("type")) == "Appartement")
            assert(cursor.getString(cursor.getColumnIndexOrThrow("address")) == "600 Maryland Ave SW, Washington, DC 20002, ??tats-Unis")
            cursor.close()

    }

    fun insertRealEstate()= runBlocking {
        id = realEstateDatabase.RealEstateDao().insert(realEstate = RealEstate(
            type = "Appartement",
            price = 1000999,
            surface = 500,
            nbRooms = 12,
            nbBathrooms = 2,
            nbBedrooms = 3,
            description = "Proche de ST Emilion, Superbe propri??t?? en pierre compos??e : d'une Girondine, 385 m2 hab., d??cor??e avec go??t, les volumes des pi??ces sont tr??s spacieux, tourn??s vers le parc de 1.3 Hectares parfaitement entretenu. Une entr??e vous m??nera ?? un salon avec grande chemin??e, prolong?? par un jardin d'hiver. Au centre de la b??tisse, une cuisine ??quip??e est encadr??e par des arches en pierre, le coin repas offre une approche moderne , une chemin??e avec insert ?? bois la compl??te. Une buanderie, une salle de bains, et wc au rez-de-chauss??e.",
            address = "600 Maryland Ave SW, Washington, DC 20002, ??tats-Unis",
            propertyStatus = false,
            dateEntry = 1.toLong(),
            dateSold = null,
            realEstateAgent = "Math",
            latitude = null,
            longitude = null,
            nearbyStore = true,
            nearbyPark = true,
            nearbyRestaurant = false,
            nearbySchool = true
        ))
    }

    fun cleanDataBase()= runBlocking{
        realEstateDatabase.RealEstateDao().allDelete()
    }
}