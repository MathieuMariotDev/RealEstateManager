package com.openclassrooms.realestatemanager.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateWithPhoto(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "idRealEstate",
                entityColumn = "idProperty"
        )
        val photos:List<Photo>
)
