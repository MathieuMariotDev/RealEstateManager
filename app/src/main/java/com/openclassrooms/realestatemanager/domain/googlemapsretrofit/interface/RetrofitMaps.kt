package com.openclassrooms.realestatemanager.domain.googlemapsretrofit.`interface`

import com.google.android.gms.common.api.internal.ApiKey
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Example
import com.openclassrooms.realestatemanager.utils.Constants.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by navneet on 17/7/16.
 */
interface RetrofitMaps {
    /*
     * Retrofit get annotation with our URL
     *
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=$API_KEY")
    fun getNearbyPlaces(
        @Query("type") type: String?,
        @Query("location") location: String?,
        @Query("radius") radius: Int
    ): Call<Example>
}