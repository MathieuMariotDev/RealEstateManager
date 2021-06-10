package com.openclassrooms.realestatemanager.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateFragment

class DetailsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putLong("idRealEstate", intent.getLongExtra("idRealEstate",0))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_details, DetailsFragment::class.java,bundle)
                .commit()
        }
    }
}