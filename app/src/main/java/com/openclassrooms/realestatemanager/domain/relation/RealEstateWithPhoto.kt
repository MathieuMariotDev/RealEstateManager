package com.openclassrooms.realestatemanager.domain.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate

data class RealEstateWithPhoto(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "idRealEstate",
                entityColumn = "idProperty"
        )
        val photos: List<Photo>?
)
