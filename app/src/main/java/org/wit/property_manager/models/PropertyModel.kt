package org.wit.property_manager.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// todo - deepen model - rental/lease status + user id of creator
@Parcelize
data class PropertyModel(var id: Long = 0, var title: String = "",
                         var description: String = "",
                         var image: Uri = Uri.EMPTY,
                         var type: String = "",
                         var status : String = "",
                        // var agent : Long = 0,
                         var lat : Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable