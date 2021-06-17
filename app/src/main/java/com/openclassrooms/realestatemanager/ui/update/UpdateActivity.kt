package com.openclassrooms.realestatemanager.ui.update

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding
import com.openclassrooms.realestatemanager.databinding.ActivityUpdateBinding
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateBinding
import com.openclassrooms.realestatemanager.domain.repository.GeocoderRepository
import com.openclassrooms.realestatemanager.ui.details.DetailsActivity
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import kotlin.properties.Delegates


class UpdateActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var updateBinding: ActivityUpdateBinding
    private val viewModel: UpdateViewModel by viewModels() {
        RealEstateViewModelFactory(
            (application as RealEstateApplication).realEstateRepository,
            photoRepository = (application as RealEstateApplication).photoRepository,
            GeocoderRepository(context = applicationContext)
        )
    }
    private var idRealEstate by Delegates.notNull<Long>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = updateBinding.root
        setContentView(view)
        setupToolbar()
        if (savedInstanceState == null) {
            val bundle = Bundle()

            bundle.putLong("idRealEstate", intent.getLongExtra("idRealEstate", 0))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_update, UpdateFragment::class.java, bundle)
                .commit()
        }
    }

    private fun observeId() {
        viewModel.liveDataIdRealEstate.observe(this, Observer {
            idRealEstate = it
        })
    }

    private fun setupToolbar() {
        mToolbar = updateBinding.materialToolbar
        mToolbar.title = "Update"
        setSupportActionBar(mToolbar)
        setupBackButton()
    }

    private fun setupBackButton() {
        mToolbar.navigationIcon = ContextCompat.getDrawable(
            this,
            R.drawable.ic_baseline_arrow_back_24
        )
        mToolbar.setNavigationOnClickListener {
            viewModel.getId()
            observeId()
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("idRealEstate", idRealEstate)
            startActivity(intent)
        }
    }
}
