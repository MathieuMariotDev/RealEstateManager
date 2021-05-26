package com.openclassrooms.realestatemanager.domain.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.domain.dao.PhotoDao
import com.openclassrooms.realestatemanager.domain.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = arrayOf(RealEstate::class, Photo::class), version = 37,exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract fun RealEstateDao(): RealEstateDao

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
        INSTANCE?.let { database ->
            scope.launch(Dispatchers.IO) {
                //deleteDatabase(database.RealEstateDao())  // Clean db for more ez dev
            }
        }
    }

    suspend fun deleteDatabase(realEstateDao: RealEstateDao) {
        // Start the app with a clean database every time.
        // Not needed if you only populate on creation.
        realEstateDao.AllDelete()

}


/**
 * Populate the database in a new coroutine.
 * If you want to start with more words, just add them.
 */


}
}

