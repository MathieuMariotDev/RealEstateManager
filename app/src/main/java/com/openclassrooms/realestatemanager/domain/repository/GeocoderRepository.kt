package com.openclassrooms.realestatemanager.domain.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.gson.internal.JavaVersion
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.errors.ApiException
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.RankBy
import com.openclassrooms.realestatemanager.BuildConfig
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException


class GeocoderRepository(private val context: Context){

    private lateinit var geocoder : Geocoder

    private val API_KEY = BuildConfig.ApiKey

    fun getLatLng(textAddress : String) : MutableList<Address>? {
        geocoder = Geocoder(context)
        return geocoder.getFromLocationName(textAddress,1)
    }




    fun getNearbyPoi(location : LatLng,placeType: PlaceType) : PlacesSearchResponse {
        var request = PlacesSearchResponse()
        val context =  GeoApiContext.Builder()
            .apiKey(API_KEY)
            .build()
        try {
            request = PlacesApi.nearbySearchQuery(context,location)
                .radius(3500)
                .type(placeType)
                .await()
        }
        catch ( error : Exception){
            when(error){
                is ApiException -> error.printStackTrace()
                is IOException -> error.printStackTrace()
                is IllegalArgumentException -> error.printStackTrace()
                else -> error.printStackTrace()
            }
        }finally {
            return request
        }

    }
}