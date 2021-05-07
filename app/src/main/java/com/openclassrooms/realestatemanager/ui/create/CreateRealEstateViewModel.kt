package com.openclassrooms.realestatemanager.ui.create

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.PhotoRepository
import com.openclassrooms.realestatemanager.domain.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate

class CreateRealEstateViewModel(private val realEstateRepository: RealEstateRepository, private val photoRepository: PhotoRepository) : ViewModel() {


    suspend fun insertRealEstate(realEstate: RealEstate){
        realEstateRepository.insertRealEstate(realEstate)
    }





}