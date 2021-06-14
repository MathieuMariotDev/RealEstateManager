package com.openclassrooms.realestatemanager.ui.details

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto

class DetailsViewModel(private val realEstateRepository: RealEstateRepository) : ViewModel() {

    lateinit var liveDataRealEstate: LiveData<RealEstateWithPhoto>

    var livedataListRealEstate  : LiveData<List<RealEstate>> = realEstateRepository.getFlowListRealEstate().asLiveData()


    val liveDataIdRealEstate: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }


    fun setId(id : Long){
        liveDataIdRealEstate.value = id
        realEstateRepository.setIdRealEstate(id)  // For modification
        liveDataRealEstate = Transformations.switchMap(liveDataIdRealEstate){ it ->
            realEstateRepository.getRealEstateWithId(it).asLiveData()
        }
    }

    fun refreshRealEstate(){

    }

}