package org.wit.property_manager.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.property_manager.main.MainApp
class PropertyMapPresenter(val view: PropertyMapsView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    suspend fun doPopulateMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(view)
        app.properties.findAll().forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    suspend fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val property = app.properties.findById(tag)
        if (property != null) view.showProperty(property)
    }
}