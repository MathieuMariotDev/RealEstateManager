package com.openclassrooms.realestatemanager.domain.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.pojo.NearbyPOI
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate

data class RealEstateWithNerbyPOI(
        @Embedded val realEstate: RealEstate,
        @Relation(
                parentColumn = "idRealEstate",
                entityColumn = "realEstateId"
        )
        val nearbyPOIs:List<NearbyPOI>
)
