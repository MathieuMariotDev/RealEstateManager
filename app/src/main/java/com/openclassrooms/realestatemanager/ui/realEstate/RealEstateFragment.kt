package com.openclassrooms.realestatemanager.ui.realEstate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateBinding
import com.openclassrooms.realestatemanager.ui.realEstate.list.RealEstateAdapter

class RealEstateFragment : Fragment(){

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private val adapter = RealEstateAdapter()
    private lateinit var realEstateBinding : FragmentRealEstateBinding
    private val viewModelFrag : RealEstateViewModel by activityViewModels() {
        RealEstateViewModelFactory(
            (requireActivity().application as RealEstateApplication).realEstateRepository,
            photoRepository = (requireActivity().application as RealEstateApplication).photoRepository,
            (requireActivity().application as RealEstateApplication).geocoderRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        realEstateBinding = FragmentRealEstateBinding.inflate(inflater,container,false)
        setupRecyclerView()
        return realEstateBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    fun setupRecyclerView() {
        recyclerView = realEstateBinding.recyclerviewRealEstate
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        realEstateBinding.recyclerviewRealEstate.adapter = adapter
        viewModelFrag.listRealEstateWithPhoto.observe(viewLifecycleOwner, Observer { listRealEstatesWithPhoto->
            listRealEstatesWithPhoto.let { adapter.data=it }
        })

    }
}