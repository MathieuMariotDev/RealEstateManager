package com.openclassrooms.realestatemanager.ui.realEstate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.ui.realEstate.list.RealEstateAdapter
import com.openclassrooms.realestatemanager.utils.debug.Mock
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    //private var textViewMain;
    //private var textViewQuantity;
    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private val viewModel: RealEstateViewModel by viewModels()
    private lateinit var listRealEstates: List<RealEstate>
    private val realEstateRepository : RealEstateRepository = RealEstateRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
        setupRecyclerView()
        setNavigationOnClick()
        setOnMenuItemClick()

        //viewModel = ViewModelProvider(this).get(RealEstateViewModel::class.java)
    }


    fun setupToolbar() {
        mToolbar = binding.materialToolbar
        setSupportActionBar(mToolbar)
    }

    fun setupRecyclerView() {
        recyclerView = binding.recyclerviewRealEstate
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        //val dividerItemDecoration = DI
        //viewModel.getLiveDataRealEstates().observe(this, listObserver)
        realEstateRepository.getRealEstates().observe(this,listObserver)
    }


    val listObserver = Observer<List<RealEstate>> { realEstates ->
        val adapter = RealEstateAdapter()
        binding.recyclerviewRealEstate.adapter = adapter
        adapter.data = realEstates  // TODO

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
                    realEstateRepository.addMockRealEstate()
                    viewModel.setLiveDataRealEstates()
                    true
                }
                else -> false
            }
        }
    }
}
