package com.openclassrooms.realestatemanager.ui.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.R.drawable.ic_baseline_arrow_back_24
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateActivity
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateFragment
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import com.openclassrooms.realestatemanager.ui.update.UpdateActivity

class DetailsActivity : AppCompatActivity(){
    private lateinit var mToolbar: Toolbar
    private lateinit var detailbinding: ActivityDetailsBinding
    private val detailsViewModel: DetailsViewModel by viewModels() {
        RealEstateViewModelFactory(
            (application as RealEstateApplication).realEstateRepository,
            photoRepository = (application as RealEstateApplication).photoRepository,
            GeocoderRepository(context = applicationContext)
        )
    }
    private var idForUpdateIntent : Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailbinding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = detailbinding.root
        setContentView(view)
        setupToolbar()
        idObserver()
        setOnMenuItemClick()
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putLong("idRealEstate", intent.getLongExtra("idRealEstate",0))
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_details, DetailsFragment::class.java,bundle)
                .commit()
        }
    }

    private fun setupToolbar() {
        mToolbar = detailbinding.materialToolbar
        mToolbar.title = "Details"
        setSupportActionBar(mToolbar)
        setupBackButton()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        menu?.findItem(R.id.realestate_filters)?.isVisible = false
        return true
    }


    private fun setupBackButton() {
        mToolbar.navigationIcon = AppCompatResources.getDrawable(this, ic_baseline_arrow_back_24)
        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setOnMenuItemClick() {
        mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.realestate_add -> {
                    //viewModel.insert()
                    val createIntent = Intent(
                        this,
                        CreateRealEstateActivity::class.java
                    )
                    startActivity(createIntent)
                    true
                }
                R.id.realestate_update -> {
                    if(idForUpdateIntent != null){
                        val updateIntent = Intent(this, UpdateActivity::class.java)
                        updateIntent.putExtra("idRealEstate",idForUpdateIntent)
                        startActivity(updateIntent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun idObserver(){
        detailsViewModel.liveDataIdRealEstate.observe(this, Observer {
            idForUpdateIntent = it
        })
    }
}