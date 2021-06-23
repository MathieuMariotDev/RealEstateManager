package com.openclassrooms.realestatemanager.domain.googlemapsretrofit.`interface`

import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.googlemapsretrofit.Example
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by navneet on 17/7/16.
 */
interface RetrofitMaps {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBD5CWCujhWa9sCDQPHrGFnJUfF0MnFtY4")
    fun getNearbyPlaces(
        @Query("type") type: String?,
        @Query("location") location: String?,
        @Query("radius") radius: Int
    ): Call<Example>
}