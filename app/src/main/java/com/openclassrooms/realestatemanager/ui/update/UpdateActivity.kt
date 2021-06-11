package com.openclassrooms.realestatemanager.ui.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R


class UpdateActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putLong("idRealEstate", intent.getLongExtra("idRealEstate",0))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_update, UpdateFragment::class.java,bundle)
                .commit()
        }
    }
    }
