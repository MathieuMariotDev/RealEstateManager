package com.openclassrooms.realestatemanager.ui.realEstate

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.model.RealEstate
import kotlinx.coroutines.launch

class RealEstateViewModel(private val realEstateRepository: RealEstateRepository) : ViewModel() {

    //private val realEstateRepository :RealEstateRepository = RealEstateRepository()
    //private var realEstates : LiveData<List<RealEstate>> = realEstateRepository.getRealEstates() //as MutableLiveData<List<RealEstate>>

    val listRealEstates: LiveData<List<RealEstate>> = realEstateRepository.getRealEstates().asLiveData()

    fun insert(/*realEstate: RealEstate*/) = viewModelScope.launch { //TODO
        realEstateRepository.addMockRealEstate()
    }


    fun getLiveDataRealEstates() = listRealEstates

    fun setLiveDataRealEstates() {
       // TODO
    }

}