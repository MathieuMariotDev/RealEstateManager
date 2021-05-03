package com.openclassrooms.realestatemanager.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateWithNerbyPOI(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "idRealEstate",
                entityColumn = "realEstateId"
        )
        val nearbyPOIs:List<NearbyPOI>
)
