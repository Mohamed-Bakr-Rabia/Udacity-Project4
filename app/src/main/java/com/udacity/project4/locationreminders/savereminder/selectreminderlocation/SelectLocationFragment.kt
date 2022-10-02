package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import kotlinx.android.synthetic.main.fragment_select_location.*
import kotlinx.android.synthetic.main.fragment_select_location.view.*
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment() ,OnMapReadyCallback{

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    lateinit var  googleMap:GoogleMap
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1001
    private val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 1002
    private val REQUEST_TURN_DEVICE_LOCATION_ON = 1003
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
//
        binding.save.setOnClickListener {
            onLocationSelected()
        }

        return binding.root
    }

    private fun onLocationSelected() {
        findNavController().navigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            true        }
        R.id.hybrid_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.map_style))

        } catch (error:Exception){

        }
        grandPermission()

        googleMap.setOnPoiClickListener {
            googleMap.addMarker(MarkerOptions().position(it.latLng).title(it.name))
            _viewModel.selectedPOI.value = it
            _viewModel.latitude.value = it.latLng.latitude
            _viewModel.longitude.value = it.latLng.longitude
            _viewModel.reminderSelectedLocationStr.value = it.name


        }

    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val location = FusedLocationProviderClient(requireActivity())
        location.lastLocation.addOnSuccessListener { myLocation ->
            if (myLocation != null)
            {
                val latLng = LatLng(myLocation.latitude,myLocation.longitude)
                googleMap.addMarker(MarkerOptions().position(latLng))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isPermissionGranted(): Boolean {
        val foregroundPermission = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermission =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundPermission && backgroundPermission
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    private fun grandPermission(){
        if (isPermissionGranted()){
            checkDeviceLocation()
        }
        else {
            var permissionArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            val resultCode = when {
                runningQOrLater -> {
                    permissionArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
                }
                else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE

            }
            ActivityCompat.requestPermissions(requireActivity(), permissionArray,resultCode)

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED ||
                (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE && grantResults[1] == PackageManager.PERMISSION_DENIED)
            ){

            _viewModel.showSnackBarInt.value = R.string.permission_denied_explanation
        } else
        {
            checkDeviceLocation()
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkDeviceLocation() {
         val locationRequest = LocationRequest.create().apply {
             priority = Priority.PRIORITY_LOW_POWER
         }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponseTask = settingClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener{ exception->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(),REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx : IntentSender.SendIntentException){
                    _viewModel.showToast.value = sendEx.message
                }
            } else {
                _viewModel.showSnackBarInt.value = R.string.location_required_error
            }

        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful)
            {
                googleMap.isMyLocationEnabled = true
                getMyLocation()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == REQUEST_TURN_DEVICE_LOCATION_ON){
            checkDeviceLocation()
        }

    }
}
