package com.openclassrooms.realestatemanager.ui.realEstate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mFragmentRealEstate: RealEstateFragment
    private lateinit var mFragmentDetails: DetailsFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private var isLargeLayout = false

    private val viewModel: RealEstateViewModel by viewModels() {
        RealEstateViewModelFactory(
            (application as RealEstateApplication).realEstateRepository,
            photoRepository = (application as RealEstateApplication).photoRepository,
            GeocoderRepository(context = applicationContext)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //viewModel = ViewModelProvider(this).get(RealEstateViewModel::class.java)

        setContentView(view)
        setupToolbar()
        setNavigationOnClick()
        setOnMenuItemClick()
        isLargeLayout = resources.getBoolean(R.bool.large_layout)
        showFragments()

    }

    fun showFragments() {
            mFragmentRealEstate = RealEstateFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_real_estate, mFragmentRealEstate)
                .commit()
        if(isLargeLayout){
            mFragmentDetails = DetailsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_details, mFragmentDetails)
                .commit()


        }
    }



    fun setupToolbar() {
        mToolbar = binding.materialToolbar
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        return true;
    }


    fun setNavigationOnClick() {
        mToolbar.setNavigationOnClickListener {
            // Handle navigation icon press

        }
    }

    fun setOnMenuItemClick() {

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
                R.id.realestate_filters -> {
                    showDialog()
                    true
                }
                else -> false
            }
        }
    }

    fun showDialog() {
        val fragmentManager = supportFragmentManager
        val newFragment = FilterDialogFragment()
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog")
        } else {
            /*// The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()*/
            FilterDialogFragment().show(fragmentManager, "test")
        }
    }
}


