package com.openclassrooms.realestatemanager.ui.loan

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding
import com.openclassrooms.realestatemanager.databinding.ActivityLoanBinding
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.details.DetailsViewModel
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity

class LoanActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var loanBinding: ActivityLoanBinding
    private val viewModel: LoanViewModel by viewModels() {
        RealEstateViewModelFactory(
            (application as RealEstateApplication).realEstateRepository,
            photoRepository = (application as RealEstateApplication).photoRepository,
            GeocoderRepository(context = applicationContext)
        )
    }
    private var checkedItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loanBinding = ActivityLoanBinding.inflate(layoutInflater)
        val view = loanBinding.root
        setContentView(view)
        setupToolbar()
        observerCurrencyId()
        setOnMenuItemClick()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_loan, LoanFragment.newInstance())
                .commit()
        }
    }


    private fun setupToolbar() {
        mToolbar = loanBinding.materialToolbar
        mToolbar.title = "Loan simulator"
        setSupportActionBar(mToolbar)
        setupBackButton()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        menu?.findItem(R.id.realestate_update)?.isVisible = false
        menu?.findItem(R.id.realestate_filters)?.isVisible = false
        menu?.findItem(R.id.realestate_add)?.isVisible = false
        menu?.findItem(R.id.loan)?.isVisible = false
        return true;
    }

    private fun setOnMenuItemClick() {
        loanBinding.materialToolbar.setOnMenuItemClickListener { menuItem ->
            // "When" for update easily
            when (menuItem.itemId) {
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

    private fun observerCurrencyId() {
        viewModel.liveDataCurrencyCode.observe(this, Observer {
            checkedItem = it
        })
    }


    private fun setupBackButton() {
        mToolbar.navigationIcon = AppCompatResources.getDrawable(
            this,
            R.drawable.ic_baseline_arrow_back_24
        )
        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}