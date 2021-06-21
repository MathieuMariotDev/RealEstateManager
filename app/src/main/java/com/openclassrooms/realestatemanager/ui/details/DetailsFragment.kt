package com.openclassrooms.realestatemanager.ui.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.PictureDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateFragment
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateViewModel
import com.openclassrooms.realestatemanager.ui.create.PhotoAdapter
import com.openclassrooms.realestatemanager.ui.realEstate.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.openclassrooms.realestatemanager.utils.PermissionsUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class DetailsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    lateinit var detailsBinding: FragmentDetailsBinding
    lateinit var recyclerView: RecyclerView
    private var adapter = PhotoAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var bitmapMarker : Bitmap

    private val viewModelDetails : DetailsViewModel by activityViewModels() {
        RealEstateViewModelFactory(
            (requireActivity().application as RealEstateApplication).realEstateRepository,
            photoRepository = (requireActivity().application as RealEstateApplication).photoRepository,
            (requireActivity().application as RealEstateApplication).geocoderRepository
        )
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewModelDetails.setId(requireArguments().getLong("idRealEstate"))
            Log.d("DEBUG", "onCreate: DetailFragment " + requireArguments().getLong("idRealEstate"))
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    fun requestPermissions() {
        if (PermissionsUtils.hasLocationPermissions(requireContext())) {
            setupMap()
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permisssions to use this app.",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        setupMap()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            addMarker(googleMap)
            googleMap.setOnMapLoadedCallback {
                moveCameraOnCurrentLocation(googleMap)
            }
            googleMap.setOnMarkerClickListener {
                viewModelDetails.setId(it.tag as Long)
                true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    private fun moveCameraOnCurrentLocation(googleMap: GoogleMap) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val bounds = LatLngBounds.builder()
            if (location != null) {
                bounds.include(
                    com.google.android.gms.maps.model.LatLng(
                        location.latitude,
                        location.longitude
                    )
                )
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        com.google.android.gms.maps.model.LatLng(
                            location.latitude,
                            location.longitude
                        ),
                        13.toFloat()
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        updateUi()
        requestPermissions()
        /*val bounds = LatLngBounds.builder()
        bounds.include(com.google.android.gms.maps.model.LatLng(47.428794860839844,-0.5276904702186584))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),20))*/
        return detailsBinding.root
    }

    private fun updateUi() {
        viewModelDetails.liveDataRealEstate.observe(viewLifecycleOwner, Observer { realEstate ->
            detailsBinding.textViewDescription.text = realEstate.realEstate.description
            detailsBinding.nbSurface.text = realEstate.realEstate.surface.toString()
            detailsBinding.textViewAdresse.text = realEstate.realEstate.address
            detailsBinding.nbRooms.text = realEstate.realEstate.nbRooms.toString()
            detailsBinding.nbBathrooms.text = realEstate.realEstate.nbBathrooms.toString()
            detailsBinding.nbBedroooms.text = realEstate.realEstate.nbBedrooms.toString()
            adapter.data = realEstate.photos!!
        })
    }

    private fun setupRecyclerView() {
        recyclerView = detailsBinding.recyclerviewPhotoDetails
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        detailsBinding.recyclerviewPhotoDetails.adapter = adapter
    }

    private fun addMarker(googleMap: GoogleMap) {
        bitmapMarker = AppCompatResources.getDrawable(requireContext(),R.drawable.ic_marker_house)!!.toBitmap()
        viewModelDetails.livedataListRealEstate.observe(
            viewLifecycleOwner,
            Observer { listRealEstate ->
                listRealEstate.forEach { realEstate ->
                    if (realEstate.latitude != null && realEstate.longitude != null) {
                        val marker = googleMap.addMarker(
                            MarkerOptions()
                                .position(
                                    com.google.android.gms.maps.model.LatLng(
                                        realEstate.latitude!!.toDouble(),
                                        realEstate.longitude!!.toDouble()
                                    )
                                ).icon(BitmapDescriptorFactory.fromBitmap(bitmapMarker))
                        )
                        marker?.tag = realEstate.idRealEstate

                    }
                }
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
    }
}