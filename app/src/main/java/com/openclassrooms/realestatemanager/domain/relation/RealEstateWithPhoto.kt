package com.openclassrooms.realestatemanager.domain.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.pojo.Photo
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate

data class RealEstateWithPhoto(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "idRealEstate",
                entityColumn = "idProperty"
        )
        val photos: Photo?
)
