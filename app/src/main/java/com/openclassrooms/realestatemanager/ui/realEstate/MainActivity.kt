package com.openclassrooms.realestatemanager.ui.realEstate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.loan.LoanActivity
import com.openclassrooms.realestatemanager.ui.update.UpdateActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var mFragmentRealEstate: RealEstateFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private var isLargeLayout = false
    private var idForUpdateIntent: Long? = null
    private var checkedItem = 0

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
        if(isLargeLayout){
            idObserver()
        }
        observerCurrencyId()
        showFragments()

    }

    fun showFragments() {
            mFragmentRealEstate = RealEstateFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_real_estate, mFragmentRealEstate)
                .commit()
    }



    fun setupToolbar() {
        mToolbar = binding.materialToolbar
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        if(!isLargeLayout){
            menu?.findItem(R.id.realestate_update)?.isVisible = false
        }
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
                R.id.realestate_update -> {

                    if (idForUpdateIntent != null) {
                        val updateIntent = Intent(this, UpdateActivity::class.java)
                        updateIntent.putExtra("idRealEstate", idForUpdateIntent)
                        startActivity(updateIntent)
                    } else {
                        Toast.makeText(this, "Please select a property", Toast.LENGTH_LONG).show()
                    }
                    true
                }
                R.id.loan -> {
                    val loanIntent = Intent(this, LoanActivity::class.java)
                    startActivity(loanIntent)
                    true
                }
                R.id.currency -> {
                    alertDialogCurrency()
                    true
                }
                else -> false
            }
        }
    }

    private fun alertDialogCurrency() {
        val items = arrayOf("Dollar", "Euro")
        MaterialAlertDialogBuilder(this)
            .setTitle("Select for convert currency to :")
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                viewModel.setCurrencyCode(which)
            }
            .show()
    }

    fun observerCurrencyId() {
        viewModel.liveDataCurrencyCode.observe(this, Observer {
            checkedItem = it
        })
    }


    fun idObserver() {
        viewModel.liveDataIdRealEstate.observe(this, Observer {
            idForUpdateIntent = it
        })
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


