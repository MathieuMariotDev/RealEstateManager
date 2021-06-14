package com.openclassrooms.realestatemanager.ui.update

import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyPOI
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.utils.PhotoFileUtils
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UpdateFragment : Fragment() {

    private lateinit var updateBinding: FragmentUpdateBinding
    private val updateViewModel: UpdateViewModel by viewModels() {
        RealEstateViewModelFactory(
            (activity?.application as RealEstateApplication).realEstateRepository,
            photoRepository = (activity?.application as RealEstateApplication).photoRepository,
            (activity?.application as RealEstateApplication).geocoderRepository
        )
    }
    private lateinit var realEstateActual: RealEstateWithPhoto
    private lateinit var adapter : PhotoUpdateAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var latlngAddress: Address? = null
    private var nearbyPOI = NearbyPOI()
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date of sale")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
    private var dateSelectedSold : Long? = null
    private lateinit var uri : Uri
    private var photo = Photo()
    private var listPhoto = ArrayList<Photo>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Log.d("DEBUG", "onCreate: DetailFragment " + requireArguments().getLong("idRealEstate"))
            updateViewModel.setId(requireArguments().getLong("idRealEstate"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateBinding = FragmentUpdateBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeRealEstate()
        onDateClicked()
        onClickPhoto()
        onClickPhotoFromFile()
        onValidationClick()
        return updateBinding.root
    }

    private fun observeRealEstate() {
        updateViewModel.liveDataRealEstate.observe(viewLifecycleOwner, Observer {
            realEstateActual = it
            adapter.data = it.photos!!
            updateUi()
        })
    }

    private fun updateUi() {
        updateBinding.textFieldAdresse.editText?.setText(realEstateActual.realEstate.address)
        updateBinding.textFieldPrice.editText?.setText(realEstateActual.realEstate.price.toString())
        updateBinding.textFieldDescription.editText?.setText(realEstateActual.realEstate.description)
        updateBinding.textFieldNbRooms.editText?.setText(realEstateActual.realEstate.nbRooms.toString())
        updateBinding.textFieldNbBedrooms.editText?.setText(realEstateActual.realEstate.nbBedrooms.toString())
        updateBinding.textFieldNbBathrooms.editText?.setText(realEstateActual.realEstate.nbBathrooms.toString())
        updateBinding.textFieldSurface.editText?.setText(realEstateActual.realEstate.surface.toString())
        updateBinding.textFieldType.editText?.setText(realEstateActual.realEstate.type)
    }


    private fun setupRecyclerView() {
        recyclerView = updateBinding.recyclerviewPhotoUpdate
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        setupAdapter()
        updateBinding.recyclerviewPhotoUpdate.adapter = adapter
    }

    fun setupAdapter(){
        adapter = PhotoUpdateAdapter() {
            if(realEstateActual.photos?.size!! > 1){
                updateViewModel.deletePhoto(it)
            }else{
                Toast.makeText(requireContext(),"At least one photo must be present",Toast.LENGTH_LONG).show()
            }
        }
    }

    private val takePitcure =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { sucess ->
            if (sucess) {
                val nameFile: Int
                uri.let {
                    requireContext().contentResolver.query(uri, null, null, null, null)
                }?.use {
                    nameFile = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    photo.path = it.getString(nameFile)
                }
                alertDialog()
            }
        }
    private fun onClickPhotoFromFile() {
       updateBinding.ButtonAddPhotoFromFolder.setOnClickListener {
            getPicture.launch("image/*")
        }
    }
    private val getPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val filename = PhotoFileUtils.createImageFile(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                photo.path = filename.name
                val fileOutputStream = FileOutputStream(filename)
                fileOutputStream.write(readBytes(uri))
                alertDialog()
            }
        }
    @Throws(IOException::class)
    private fun readBytes(uri: Uri): ByteArray? =
        requireContext().contentResolver.openInputStream(uri)?.buffered().use {
            it?.readBytes()
        }

    private fun onClickPhoto() {
        updateBinding.ButtonAddPhoto.setOnClickListener {
            uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                PhotoFileUtils.createImageFile(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            )
            takePitcure.launch(uri)
        }
    }

    private fun alertDialog() {
        var editText = EditText(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Enter Photo Name")
            .setView(editText)
            .setPositiveButton(requireContext().resources.getString(R.string.validate)) { dialog, _ ->
                if (!editText.text.isNullOrEmpty()) {
                    photo.label = editText.text.toString()
                    photo.idProperty = realEstateActual.realEstate.idRealEstate
                    updateViewModel.insertPhoto(photo)
                    photo = Photo()
                    dialog.dismiss()
                }
            }
            .show()
    }

    private fun onValidationClick() {
        updateBinding.ButtonUpdate.setOnClickListener {
            if(updateBinding.textFieldAdresse.editText?.text.toString() != realEstateActual.realEstate.address){
            updateViewModel.getLatLng(updateBinding.textFieldAdresse.editText?.text.toString())
            updateViewModel.liveDataAddress.observe(viewLifecycleOwner, Observer { it ->
                if (it.isNullOrEmpty()) {
                    updateViewModel.getNearbyPoi()
                } else {
                    latlngAddress = it[0]
                    updateViewModel.getNearbyPoi(
                        LatLng(
                            latlngAddress!!.latitude,
                            latlngAddress!!.longitude
                        )
                    )
                }
                updateViewModel.liveDataNearbyPOI.observe(viewLifecycleOwner, Observer {liveDataNearbyPOI ->
                    nearbyPOI = liveDataNearbyPOI
                    updateRealEstate()
                })
            })

            }else{
                nearbyPOI.nearbyPark = realEstateActual.realEstate.nearbyPark
                nearbyPOI.nearbyRestaurant = realEstateActual.realEstate.nearbyRestaurant
                nearbyPOI.nearbySchool = realEstateActual.realEstate.nearbySchool
                nearbyPOI.nearbyStore = realEstateActual.realEstate.nearbyStore
                if(realEstateActual.realEstate.latitude != null && realEstateActual.realEstate.longitude != null){
                    latlngAddress?.latitude  = realEstateActual.realEstate.latitude!!.toDouble()
                    latlngAddress?.longitude  = realEstateActual.realEstate.longitude!!.toDouble()
                }
                updateRealEstate()

            }
        }
    }


    private fun updateRealEstate(){
        val realEstateToUpdate = RealEstate(
            idRealEstate = realEstateActual.realEstate.idRealEstate,
            type = updateBinding.textFieldType.editText?.text.toString(),
            price = updateBinding.textFieldPrice.editText?.text.toString()
                .toInt(),
            surface = updateBinding.textFieldSurface.editText?.text.toString()
                .toFloat(),
            nbRooms = updateBinding.textFieldNbRooms.editText?.text.toString()
                .toInt(),
            nbBathrooms = updateBinding.textFieldNbBathrooms.editText?.text.toString()
                .toInt(),
            nbBedrooms = updateBinding.textFieldNbBedrooms.editText?.text.toString()
                .toInt(),
            description = updateBinding.textFieldDescription.editText?.text.toString(),
            address = updateBinding.textFieldAdresse.editText?.text.toString(),
            propertyStatus = false,
            dateEntry = realEstateActual.realEstate.dateEntry,
            dateSold = dateSold(),
            realEstateAgent = realEstateActual.realEstate.realEstateAgent,
            latitude = latlngAddress?.longitude?.toFloat(),
            longitude =latlngAddress?.latitude?.toFloat(),
            nearbyStore = nearbyPOI.nearbyStore,
            nearbyPark = nearbyPOI.nearbyPark,
            nearbyRestaurant = nearbyPOI.nearbyRestaurant,
            nearbySchool = nearbyPOI.nearbySchool
        )
        if (realEstateActual.realEstate == realEstateToUpdate) {
            Toast.makeText(requireContext(), "No modification", Toast.LENGTH_LONG)
                .show()
        } else {
            updateViewModel.updateRealEstate(realEstateToUpdate)
        }
    }

    private fun onDateClicked() {
        updateBinding.ButtonDatePicker.setOnClickListener {
            val fragmentManager = parentFragmentManager
            datePicker.show(fragmentManager,"DatePicker")
            datePicker.addOnPositiveButtonClickListener { selection: Long->
                dateSelectedSold = Date(selection).time
            }
        }
    }

    private fun dateSold(): Long? = dateSelectedSold
}