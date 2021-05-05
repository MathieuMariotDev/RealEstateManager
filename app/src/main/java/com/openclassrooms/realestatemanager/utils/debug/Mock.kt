package com.openclassrooms.realestatemanager.utils.debug

import android.net.Uri
import com.openclassrooms.realestatemanager.R
import kotlin.random.Random

class Mock {


    fun getRandomPropertyType(): String {
        val randomType = Random.nextInt(PropertyType.values().size)
        return PropertyType.values()[randomType].toString()
    }


    fun getRandomPrice() = Random.nextInt(100000, 10000000)

    fun getRandomSurface() = Random.nextInt(30,1000) + Random.nextFloat()

    fun getRandomNbRooms() = Random.nextInt(2,15)

    fun getDefaultDescription() = "Proche de ST Emilion, Superbe propriété en pierre composée : d'une Girondine, 385 m2 hab., décorée avec goût, les volumes des pièces sont très spacieux, tournés vers le parc de 1.3 Hectares parfaitement entretenu. Une entrée vous mènera à un salon avec grande cheminée, prolongé par un jardin d'hiver. Au centre de la bâtisse, une cuisine équipée est encadrée par des arches en pierre, le coin repas offre une approche moderne , une cheminée avec insert à bois la complète. Une buanderie, une salle de bains, et wc au rez-de-chaussée."

    fun getRandomAddress(): String {
        val randomAddress = Random.nextInt(PropertyAddress.values().size)
        return PropertyAddress.values()[randomAddress].address
    }

    fun getRandomNearbyPOI() = Random.nextBoolean()

    fun getRandomPhoto(): Int {
        val randomPhoto = Random.nextInt(PropertyPhoto.values().size)

        return PropertyPhoto.values()[randomPhoto].photoPath
    }

    enum class PropertyPhoto(val photoPath: Int){
        Photo1(R.drawable.house1),
        Photo2(R.drawable.house2),
        Photo3(R.drawable.house3)
    }



    enum class PropertyAddress(val address: String) {
        Address1("600 Maryland Ave SW, Washington, DC 20002, États-Unis"),
        Address2("700 L'Enfant Plaza SW, Washington, DC 20024, États-Unis"),
        Address3("935 Pennsylvania Avenue NW, Washington, DC 20535, États-Unis"),
        Address4("3500 East-West Hwy, Hyattsville, MD 20782, États-Unis"),
        Address5("110 Irving St NW, Washington, DC 20010, États-Unis")
    }

    enum class PropertyType() {
        Apartment,
        Loft,
        Manoir,
        Castle,
        Duplex,
        House
    }


}
