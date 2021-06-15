package com.openclassrooms.realestatemanager.ui.create

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.AUTOFILL_HINT_NAME
import android.view.View.AUTOFILL_HINT_POSTAL_ADDRESS
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentCreateRealEstateBinding
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import com.openclassrooms.realestatemanager.utils.Notification
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [CreateRealEstateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateRealEstateFragment : Fragment() {

    lateinit var bitmap: Bitmap
    private lateinit var uri: Uri
    private val mock: Boolean = false
    private var listPhoto = ArrayList<Photo>()
    private lateinit var notification: Notification
    private var latlng: Address? = null
    private var photo = Photo()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var adapter = PhotoAdapter()
    private lateinit var createBinding: FragmentCreateRealEstateBinding
    private var alertDialogNoNetworkSaw = false

    companion object {
        fun newInstance() = CreateRealEstateFragment()
    }


    private val viewModel: CreateRealEstateViewModel by viewModels() {
        RealEstateViewModelFactory(
            (activity?.application as RealEstateApplication).realEstateRepository,
            photoRepository = (activity?.application as RealEstateApplication).photoRepository,
            (activity?.application as RealEstateApplication).geocoderRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        createBinding = FragmentCreateRealEstateBinding.inflate(inflater, container, false)
        autoFillHints()
        onClickAdd()
        onClickPhoto()
        onClickPhotoFromFile()
        setupRecyclerView()
        return createBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notification = Notification(context)
    }

    fun autoFillHints() { // TODO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Not supported above
            createBinding.textFieldAdresse.setAutofillHints(AUTOFILL_HINT_POSTAL_ADDRESS)
            createBinding.textFieldRealEstateAgent.setAutofillHints(AUTOFILL_HINT_NAME)
        }
    }

    private fun alertDialogNoNetwork() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("The marker and nearby points of interest will not be available for this property")
            .setTitle("Network not available")
            .setPositiveButton("Ok") { dialog, _ ->
                alertDialogNoNetworkSaw = true

            }
            .show()
    }

    fun onClickAdd() {
        createBinding.ButtonAdd.setOnClickListener {
            if (validate()) {
                if (Utils.isInternetAvailable(requireContext()) || alertDialogNoNetworkSaw) {
                    getLatLong()
                    viewModel.liveDataAddress.observe(
                        viewLifecycleOwner,
                        Observer { liveDataAddress ->
                            if (liveDataAddress.isNullOrEmpty()) {
                                viewModel.getNearbyPoi()
                            } else {
                                latlng = liveDataAddress[0]
                                Log.d(
                                    "LatLong geocoder",
                                    "getLatLong:" + latlng!!.latitude + latlng!!.longitude
                                )
                                viewModel.getNearbyPoi(
                                    LatLng(
                                        latlng!!.latitude,
                                        latlng!!.longitude
                                    )
                                )
                            }
                            viewModel.liveDataNearbyPOI.observe(
                                viewLifecycleOwner,
                                Observer { liveDataNearbyPOI ->
                                    val realEstate = RealEstate(
                                        type = createBinding.textFieldType.editText?.text.toString(),
                                        price = createBinding.textFieldPrice.editText?.text.toString()
                                            .toInt(),
                                        surface = createBinding.textFieldSurface.editText?.text.toString()
                                            .toFloat(),
                                        nbRooms = createBinding.textFieldNbRooms.editText?.text.toString()
                                            .toInt(),
                                        nbBathrooms = createBinding.textFieldNbBathrooms.editText?.text.toString()
                                            .toInt(),
                                        nbBedrooms = createBinding.textFieldNbBedrooms.editText?.text.toString()
                                            .toInt(),
                                        description = createBinding.textFieldDescription.editText?.text.toString(),
                                        address = createBinding.textFieldAdresse.editText?.text.toString(),
                                        propertyStatus = false,
                                        dateEntry = Utils.getTodayDateInLong(Utils.getTodayDate()),
                                        dateSold = null,
                                        realEstateAgent = createBinding.textFieldRealEstateAgent.editText?.text.toString(),
                                        latitude = latlng?.latitude?.toFloat(),
                                        longitude = latlng?.longitude?.toFloat(),
                                        nearbyStore = liveDataNearbyPOI.nearbyStore,
                                        nearbyPark = liveDataNearbyPOI.nearbyPark,
                                        nearbyRestaurant = liveDataNearbyPOI.nearbyRestaurant,
                                        nearbySchool = liveDataNearbyPOI.nearbySchool
                                    )
                                    viewModel.insertRealEstate(realEstate)
                                })
                        })
                    viewModel.liveData.observe(viewLifecycleOwner, Observer { livedata ->
                        notificationIfAddCorrectly()
                        for (photoItem in listPhoto) {
                            val photo = Photo(
                                path = photoItem.path,
                                label = photoItem.label,
                                idProperty = livedata
                            )
                            viewModel.insertPhoto(photo)
                        }
                    })
                } else {
                    alertDialogNoNetwork()
                }
            }
        }
    }

    private fun onClickPhoto() {
        createBinding.ButtonAddPhoto.setOnClickListener {
            uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                createImageFile()
            )
            takePitcure.launch(uri)
        }
    }

    private fun onClickPhotoFromFile() {
        createBinding.ButtonAddPhotoFromFolder.setOnClickListener {
            getPicture.launch("image/*")
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

    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat.getDateTimeInstance().format(Date()).replace(":", "").replace("?", "")
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }


    private val getPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val filename = createImageFile()
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


    private fun getLatLong() {
        val address = createBinding.textFieldAdresse.editText?.text.toString()
        viewModel.getLatLng(address)
    }

    private fun notificationIfAddCorrectly() { //TODO
            notification.createNotificationChannel()
            notification.buildNotif()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
    }


    private fun alertDialog() {
        var editText = EditText(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Enter Photo Name")
            .setView(editText)
            .setPositiveButton(requireContext().resources.getString(R.string.validate)) { dialog, _ ->
                if (!editText.text.isNullOrEmpty()) {
                    photo.label = editText.text.toString()
                    listPhoto.add(photo)
                    viewModel.setPhoto(listPhoto)
                    photo = Photo()
                    dialog.dismiss()
                }
            }

            .show()
    }

    override fun onResume() {
        super.onResume()
    }




    private fun updateRecycler() {
        viewModel.liveDataListPhoto.observe(viewLifecycleOwner, Observer {
            it.let { adapter.data = it ;listPhoto = it}
        })
    }

    private fun setupRecyclerView() {
        recyclerView = createBinding.recyclerviewPhoto
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = linearLayoutManager
        createBinding.recyclerviewPhoto.adapter = adapter
        updateRecycler()
    }

    private fun hasText(textInputLayout: TextInputLayout, error_message: String): Boolean {
        val text = textInputLayout.editText?.text.toString().trim()
        textInputLayout.error = null
        if (text.isEmpty()) {
            textInputLayout.error = error_message
            return false
        }
        return true
    }

    private fun validate(): Boolean {
        var check = true
        if (!hasText(createBinding.textFieldAdresse, "This field must be completed")) check = false
        if (!hasText(createBinding.textFieldType, "This field must be completed")) check = false
        if (!hasText(createBinding.textFieldNbBathrooms, "This field must be completed")) check =
            false
        if (!hasText(createBinding.textFieldNbBedrooms, "This field must be completed")) check =
            false
        if (!hasText(createBinding.textFieldNbRooms, "This field must be completed")) check = false
        if (!hasText(createBinding.textFieldPrice, "This field must be completed")) check = false
        if (!hasText(
                createBinding.textFieldRealEstateAgent,
                "This field must be completed"
            )
        ) check = false
        if (!hasText(createBinding.textFieldSurface, "This field must be completed")) check = false
        return check
    }
}