package com.openclassrooms.realestatemanager.ui.update

import android.content.Intent
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
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
import com.openclassrooms.realestatemanager.ui.details.DetailsViewModel
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import com.openclassrooms.realestatemanager.utils.PhotoFileUtils
import com.openclassrooms.realestatemanager.utils.TextFieldUtils.Companion.hasText
import com.openclassrooms.realestatemanager.utils.TextFieldUtils.Companion.isNumber
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UpdateFragment : Fragment() {

    private lateinit var updateBinding: FragmentUpdateBinding
    private val updateViewModel: UpdateViewModel by activityViewModels() {
        RealEstateViewModelFactory(
            (requireActivity().application as RealEstateApplication).realEstateRepository,
            photoRepository = (requireActivity().application as RealEstateApplication).photoRepository,
            (requireActivity().application as RealEstateApplication).geocoderRepository
        )
    }
    private lateinit var realEstateActual: RealEstateWithPhoto
    private lateinit var adapter: PhotoUpdateAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var latlngAddress: Address? = null
    private var nearbyPOI = NearbyPOI()
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date of sale")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
    private var dateSelectedSold: Long? = null
    private lateinit var uri: Uri
    private var photo = Photo()
    private var listPhoto = ArrayList<Photo>()
    private var showDialog = true
    private var alertDialogNoNetworkSaw = false
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
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = linearLayoutManager
        setupAdapter()
        updateBinding.recyclerviewPhotoUpdate.adapter = adapter
    }

    fun setupAdapter() {
        adapter = PhotoUpdateAdapter() {
            alertDialogUpdateOrDelete(it)
        }

    }

    fun alertDialogUpdateOrDelete(photo: Photo) {
        var editText = EditText(requireContext())
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Enter Photo Name")
            .setView(editText)
            .setPositiveButton("Update") { dialog, _ ->
                if (!editText.text.isNullOrEmpty()) {
                    photo.label = editText.text.toString()
                    photo.idProperty = realEstateActual.realEstate.idRealEstate
                    updateViewModel.updatePhoto(photo)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Delete definitely") { dialog, _ ->
                if (realEstateActual.photos?.size!! > 1) {
                    updateViewModel.deletePhoto(photo)
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "At least one photo must be present",
                        Toast.LENGTH_LONG
                    ).show()
                    dialog.dismiss()
                }

            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
                alertDialogPhoto()
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
                val filename =
                    PhotoFileUtils.createImageFile(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                photo.path = filename.name
                val fileOutputStream = FileOutputStream(filename)
                fileOutputStream.write(readBytes(uri))
                alertDialogPhoto()
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

    private fun alertDialogPhoto() {
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

    private fun alertDialogBadAdresseLocation() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("The address is invalid")
            .setMessage("Some functionality such as the display of marker on the map and nearby points of interest will therefore not be available for this property.")
            .setNeutralButton("Ok") { dialog, _ ->
                intentMainActivity()
                dialog.dismiss()
            }
            .setPositiveButton("Stay and update") { dialog, _ ->
                dialog.dismiss()
                showDialog = true
            }
            .show()
    }

    private fun onValidationClick() {
        updateBinding.ButtonUpdate.setOnClickListener {
            if (validate()) {
                if (Utils.isInternetAvailable(requireContext())) {
                    if (updateBinding.textFieldAdresse.editText?.text.toString() != realEstateActual.realEstate.address) {
                        updateViewModel.getLatLng(updateBinding.textFieldAdresse.editText?.text.toString())
                        updateViewModel.liveDataAddress.observe(viewLifecycleOwner, Observer { it ->
                            if (it.isNullOrEmpty()) {
                                showDialog = false
                                alertDialogBadAdresseLocation()
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
                            updateViewModel.liveDataNearbyPOI.observe(
                                viewLifecycleOwner,
                                Observer { liveDataNearbyPOI ->
                                    nearbyPOI = liveDataNearbyPOI
                                    updateRealEstate()
                                })
                        })
                    } else {
                        noUpdatePoiAndLocation()
                    }
                } else {
                    if (!alertDialogNoNetworkSaw) {
                        alertDialogNoNetwork()
                    } else {
                        noUpdatePoiAndLocation()
                    }
                }
            }
        }
    }

    private fun noUpdatePoiAndLocation() {
        nearbyPOI.nearbyPark = realEstateActual.realEstate.nearbyPark
        nearbyPOI.nearbyRestaurant = realEstateActual.realEstate.nearbyRestaurant
        nearbyPOI.nearbySchool = realEstateActual.realEstate.nearbySchool
        nearbyPOI.nearbyStore = realEstateActual.realEstate.nearbyStore
        if (realEstateActual.realEstate.latitude != null && realEstateActual.realEstate.longitude != null) {
            latlngAddress?.latitude =
                realEstateActual.realEstate.latitude!!.toDouble()
            latlngAddress?.longitude =
                realEstateActual.realEstate.longitude!!.toDouble()
        }
        updateRealEstate()
    }

    private fun updateRealEstate() {
        val realEstateToUpdate = RealEstate(
            idRealEstate = realEstateActual.realEstate.idRealEstate,
            type = updateBinding.textFieldType.editText?.text.toString(),
            price = updateBinding.textFieldPrice.editText?.text.toString()
                .toInt(),
            surface = updateBinding.textFieldSurface.editText?.text.toString()
                .toInt(),
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
            longitude = latlngAddress?.latitude?.toFloat(),
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
            if (showDialog) {
                intentMainActivity()
            }
        }
    }

    fun intentMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun onDateClicked() {
        updateBinding.ButtonDatePicker.setOnClickListener {
            val fragmentManager = parentFragmentManager
            datePicker.show(fragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { selection: Long ->
                dateSelectedSold = Date(selection).time
            }
        }
    }

    private fun dateSold(): Long? = dateSelectedSold

    private fun validate(): Boolean {
        var check = true
        if (!hasText(updateBinding.textFieldAdresse, "This field must be completed")) check = false
        if (!hasText(updateBinding.textFieldType, "This field must be completed")) check = false
        if (!hasText(
                updateBinding.textFieldNbBathrooms,
                "This field must be completed"
            ) || !isNumber(
                updateBinding.textFieldNbBathrooms,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(
                updateBinding.textFieldNbBedrooms,
                "This field must be completed"
            ) || !isNumber(
                updateBinding.textFieldNbBedrooms,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(updateBinding.textFieldNbRooms, "This field must be completed") || !isNumber(
                updateBinding.textFieldNbRooms,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(updateBinding.textFieldPrice, "This field must be completed") || !isNumber(
                updateBinding.textFieldPrice,
                "This field must only contain numbers"
            )
        ) check = false
        if (!hasText(updateBinding.textFieldSurface, "This field must be completed") || !isNumber(
                updateBinding.textFieldSurface,
                "This field must only contain numbers"
            )
        ) check = false
        return check
    }

    private fun alertDialogNoNetwork() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("The marker for the location and nearby points of interest will not be available for this property")
            .setTitle("Network not available")
            .setPositiveButton("Ok") { dialog, _ ->
                alertDialogNoNetworkSaw = true

            }
            .show()
    }
}