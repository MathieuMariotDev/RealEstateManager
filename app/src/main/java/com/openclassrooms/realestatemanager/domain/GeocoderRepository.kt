package com.openclassrooms.realestatemanager.domain

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.RankBy
import java.lang.Exception


class GeocoderRepository(private val context: Context){

    private lateinit var geocoder : Geocoder


    fun getLatLng(textAddress : String) : MutableList<Address>? {
        geocoder = Geocoder(context)
        return geocoder.getFromLocationName(textAddress,1)
    }

    fun getNearbyPoi(location : LatLng) : PlacesSearchResponse {
        var request = PlacesSearchResponse()
        val context =  GeoApiContext.Builder().apiKey(com.openclassrooms.realestatemanager.BuildConfig.ApiKey).build()
        try {
            request = PlacesApi.nearbySearchQuery(context,location)
                .radius(5000)
                .rankby(RankBy.PROMINENCE)
                .language("en")
                .await()
        }
        catch ( error : Exception){
            error.printStackTrace()
        }finally {
            return request
        }

    }
}