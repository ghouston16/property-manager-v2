package org.wit.property_manager.views.property


import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.property_manager.helpers.checkLocationPermissions
import org.wit.property_manager.helpers.createDefaultLocationRequest
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.Location
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.helpers.showImagePicker
import org.wit.property_manager.views.location.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class PropertyPresenter(private val view: PropertyView) {
    var map: GoogleMap? = null
    var property = PropertyModel()

    var app: MainApp = view.application as MainApp

    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false

    private val location = Location(52.245696, -7.139102, 15f)
    val locationRequest = createDefaultLocationRequest()

    init {
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("property_edit")) {
            edit = true
            property = view.intent.extras?.getParcelable("property_edit")!!
            view.showProperty(property)
        }
        else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            property.lat = location.lat
            property.lng = location.lng
        }
    }


    fun doAddOrSave(title: String, description: String, type: String, status: String) {
        property.title = title
        property.description = description
        property.type = type
        property.status = status
        if (edit) {
            app.properties.update(property)
        } else {
            app.properties.create(property)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.properties.delete(property)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (property.zoom != 0f) {
            location.lat =  property.lat
            location.lng = property.lng
            location.zoom = property.zoom
            locationUpdate(property.lat, property.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun cacheProperty (title: String, description: String, type: String, status: String) {
        property.title = title;
        property.description = description
        property.type = type
        property.status = status

    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(property.lat, property.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        property.lat = lat
        property.lng = lng
        property.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(property.title).position(LatLng(property.lat, property.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(property.lat, property.lng), property.zoom))
        view.showProperty(property)
    }
    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }



    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            property.image = result.data!!.data!!
                            view.updateImage(property.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            property.lat = location.lat
                            property.lng = location.lng
                            property.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}