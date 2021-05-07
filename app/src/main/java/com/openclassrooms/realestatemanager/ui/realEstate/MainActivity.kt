package com.openclassrooms.realestatemanager.ui.realEstate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateActivity
import com.openclassrooms.realestatemanager.ui.realEstate.list.RealEstateAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private val adapter = RealEstateAdapter()

    private val viewModel: RealEstateViewModel by viewModels(){
        RealEstateViewModelFactory((application as RealEstateApplication).realEstateRepository,photoRepository = (application as RealEstateApplication).photoRepository)
    }
    private lateinit var listRealEstates: List<RealEstate>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
        setupRecyclerView()
        setNavigationOnClick()
        setOnMenuItemClick()

    }


    fun setupToolbar() {
        mToolbar = binding.materialToolbar
        setSupportActionBar(mToolbar)
    }

    fun setupRecyclerView() {
        recyclerView = binding.recyclerviewRealEstate
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        binding.recyclerviewRealEstate.adapter = adapter
        viewModel.listRealEstateWithPhoto.observe(this, Observer { listRealEstatesWithPhoto->
            listRealEstatesWithPhoto?.let { adapter.data=it }
        })

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
                    val createIntent = Intent(this,
                            CreateRealEstateActivity::class.java)
                    startActivity(createIntent)
                    true
                }
                else -> false
            }
        }
    }
}
