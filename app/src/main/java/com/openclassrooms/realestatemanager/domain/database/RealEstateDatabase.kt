package com.openclassrooms.realestatemanager.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.domain.dao.NearbyPoiDao
import com.openclassrooms.realestatemanager.domain.dao.PhotoDao
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.model.NearbyPOI
import com.openclassrooms.realestatemanager.domain.model.Photo
import com.openclassrooms.realestatemanager.domain.model.RealEstate
import kotlinx.coroutines.CoroutineScope


@Database(entities = arrayOf(RealEstate::class, NearbyPOI::class, Photo::class), version = 1)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract fun RealEstateDao(): RealEstateDao

    abstract fun NearbyPoiDao(): NearbyPoiDao

    abstract fun PhotoDao(): PhotoDao


    companion object {
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null


        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): RealEstateDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        RealEstateDatabase::class.java,
                "real_estate_database"
                )
                        .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                        .addCallback(RealEstateDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }


//* NOT ACTUALY USED //
private class RealEstateDatabaseCallback(
        private val scope: CoroutineScope
) : RoomDatabase.Callback() {
    /**
     * Override the onCreate method to populate the database.
     */
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // If you want to keep the data through app restarts,
        // comment out the following line.
        /*INSTANCE?.let { database ->
            scope.launch(Dispatchers.IO) {
                //populateDatabase(database.wordDao())
            }
        }*/
    }
}


/**
 * Populate the database in a new coroutine.
 * If you want to start with more words, just add them.
 */

/*suspend fun populateDatabase(realEstateDao: RealEstateDao) {
    // Start the app with a clean database every time.
    // Not needed if you only populate on creation.


}
}
*/
}