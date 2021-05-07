package com.openclassrooms.realestatemanager.ui.create

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.AUTOFILL_HINT_NAME
import android.view.View.AUTOFILL_HINT_POSTAL_ADDRESS
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat.setAutofillHints
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentCreateRealEstateBinding
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateRealEstateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateRealEstateFragment : Fragment() {


    private lateinit var createBinding: FragmentCreateRealEstateBinding


    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createBinding = FragmentCreateRealEstateBinding.inflate(inflater, container, false)
        autoFillHints()
        onClickAdd()
        return createBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O) // Not supported above // TODO
    fun autoFillHints() {
        createBinding.textViewAdresse.setAutofillHints(AUTOFILL_HINT_POSTAL_ADDRESS)
        createBinding.textFieldRealEstateAgent.setAutofillHints(AUTOFILL_HINT_NAME)
    }

    fun onClickAdd() {
        createBinding.ButtonAdd.setOnClickListener {
            val realEstate = RealEstate(
                    type = createBinding.textFieldType.editText?.text.toString(),
                    price = createBinding.textFieldPrice.editText?.text.toString().toInt(),
                    surface = createBinding.textFieldSurface.editText?.text.toString().toFloat(),
                    nbRooms = createBinding.textFieldNbRooms.editText?.text.toString().toInt(),
                    nbBathrooms = createBinding.textFieldNbBathrooms.editText?.text.toString().toInt(),
                    nbBedrooms = createBinding.textFieldNbBedrooms.editText?.text.toString().toInt(),
                    description = createBinding.textFieldDescription.editText?.text.toString(),
                    address = createBinding.textFieldAdresse.editText?.text.toString(),
                    propertyStatus = false,
                    dateEntry = null,
                    dateSale = null,
                    realEstateAgent = createBinding.textFieldRealEstateAgent.editText?.text.toString(),
                    latitude = null,
                    longitude = null,
                    nearbyStore = null,
                    nearbyPark = null,
                    nearbyRestaurant = null,
                    nearbySchool = null
            )
        }
    }


}