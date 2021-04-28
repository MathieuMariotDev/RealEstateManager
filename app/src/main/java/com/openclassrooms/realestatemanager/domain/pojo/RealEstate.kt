package com.openclassrooms.realestatemanager.domain.pojo

import android.net.Uri
import java.util.*

data class RealEstate(var type:String,
                      var price:Int,
                      var surface:Float,
                      var nbRooms:Int,
                      var description:String,
                      var listPhoto: List<Int>,
                      var address:String,
                      var nearbyPOI:List<String>?,
                      var propertyStatus:Boolean,
                      var dateEntry: Date?,
                      var dateSale: Date?,
                      var realEstateAgent:String?)
