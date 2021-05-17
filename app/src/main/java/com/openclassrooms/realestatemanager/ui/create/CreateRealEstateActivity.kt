package com.openclassrooms.realestatemanager.ui.create
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateFragment
import java.net.URLClassLoader.newInstance

class CreateRealEstateActivity : AppCompatActivity() {

    var createRealEstateFragment = CreateRealEstateFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_real_estate)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view,CreateRealEstateFragment.newInstance())
                    .commit()
        }
    }
}