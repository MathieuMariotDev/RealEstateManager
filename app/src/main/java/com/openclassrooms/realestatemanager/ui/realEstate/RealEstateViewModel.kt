package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate

class RealEstateViewModel : ViewModel() {

    private val realEstateRepository :RealEstateRepository = RealEstateRepository()
    private var realEstates : MutableLiveData<List<RealEstate>> = realEstateRepository.getRealEstates()  //as MutableLiveData<List<RealEstate>>



    fun getLiveDataRealEstates() = realEstates

    fun setLiveDataRealEstates() {
       // TODO
    }

}