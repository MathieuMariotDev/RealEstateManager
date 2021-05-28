package com.openclassrooms.realestatemanager.domain.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate

data class RealEstateWithPhoto(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "id_realestate",
                entityColumn = "id_property"
        )
        val photos: List<Photo>?
)
