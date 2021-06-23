package com.openclassrooms.realestatemanager.domain.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Example
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Result
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.`interface`.RetrofitMaps
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.RuntimeException


class GeocoderRepository(private val context: Context) {

    private lateinit var geocoder: Geocoder


    fun getLatLng(textAddress: String): MutableList<Address>? {
        geocoder = Geocoder(context)
        return try {
            geocoder.getFromLocationName(textAddress, 1)
        } catch (error: IOException) {
            null
        }
    }


    fun getNearbyPoi(location: LatLng, placeType: String): Response<Example>? {
        val latLng: String = location.latitude.toString() + "," + location.longitude.toString()
        var retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service: RetrofitMaps = retrofit.create(RetrofitMaps::class.java)
        var call: Call<Example> = service.getNearbyPlaces(placeType, latLng, 1500)

        try {
            return call.execute()
        } catch (error: Exception) {
            when (error) {
                is ApiException -> error.printStackTrace()
                is IOException -> error.printStackTrace()
                is IllegalArgumentException -> error.printStackTrace()
                else -> error.printStackTrace()
            }
        }
        return null
    }
}

