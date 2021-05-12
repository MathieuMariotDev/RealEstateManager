package com.openclassrooms.realestatemanager.ui.create

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.AUTOFILL_HINT_NAME
import android.view.View.AUTOFILL_HINT_POSTAL_ADDRESS
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentCreateRealEstateBinding
import com.openclassrooms.realestatemanager.domain.pojo.Photo
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    lateinit var bitmap: Bitmap

    //private lateinit var uri : Uri
    private lateinit var uri : Uri

    private val mock : Boolean = false
    private var listFileName = ArrayList<String>()

    private val getPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            if (uri != null) {
                val source =
                    ImageDecoder.createSource(requireActivity().application.contentResolver, uri)
                bitmap = ImageDecoder.decodeBitmap(source)
                createBinding.imageViewTest.setImageBitmap(bitmap)
            }

        }


    private val viewModel: CreateRealEstateViewModel by viewModels() {
        RealEstateViewModelFactory(
            (activity?.application as RealEstateApplication).realEstateRepository,
            photoRepository = (activity?.application as RealEstateApplication).photoRepository
        )
    }
    private lateinit var createBinding: FragmentCreateRealEstateBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        createBinding = FragmentCreateRealEstateBinding.inflate(inflater, container, false)
        autoFillHints()
        onClickAdd()
        onClickPhoto()
        return createBinding.root
    }



    fun autoFillHints() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // Not supported above
        createBinding.textViewAdresse.setAutofillHints(AUTOFILL_HINT_POSTAL_ADDRESS)
        createBinding.textFieldRealEstateAgent.setAutofillHints(AUTOFILL_HINT_NAME)
        }
    }

    fun onClickAdd() {
        createBinding.ButtonAdd.setOnClickListener {

            if(BuildConfig.DEBUG && mock){
                viewModel.insertMockRealEstate()
            }
            else{
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
                viewModel.insertRealEstate(realEstate)
            }
            viewModel.liveData.observe(viewLifecycleOwner,Observer { livedata ->
                if(BuildConfig.DEBUG && mock){
                    viewModel.insertMockPhoto()
                }
                else{
                    val photo = Photo(
                        path = listFileName[0],
                        label = null,
                        idProperty = livedata
                    )
                    viewModel.insertPhoto(photo)
                }
                val intent = Intent(requireContext(),MainActivity::class.java)
                startActivity(intent)
            })
        }
    }

    private fun onClickPhoto() {
        createBinding.ButtonAddPhoto.setOnClickListener {
            uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID +".provider",
                createImageFile()
            )
            takePitcure.launch(uri)

        }
    }




    private val takePitcure =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { sucess ->
            if (sucess) {
                val nameFile : Int
                /*val source =
                    ImageDecoder.createSource(requireActivity().application.contentResolver, uri)
                bitmap = ImageDecoder.decodeBitmap(source)
                createBinding.imageViewTest.setImageBitmap(bitmap)*/


                uri.let {
                    requireContext().contentResolver.query(uri,null,null,null,null)
                }?.use {
                    nameFile = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    listFileName.add(it.getString(nameFile))
                    Glide.with(requireActivity())
                        .load(File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),listFileName[0]))
                        .centerCrop()
                        .into(createBinding.imageViewTest)

                }



            }

        }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date()).trim()
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

}