package org.wit.property_manager.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// todo - deepen model - rental/lease status + user id of creator
@Parcelize
@Entity
data class PropertyModel(@PrimaryKey(autoGenerate = true)var id: Long = 0, var title: String = "",
                         var fbId: String = "",
                         var description: String = "",
                         var image: String = "",
                         var type: String = "",
                         var status : String = "",
                         var favourite: Boolean= false,
                        // var agent : Long = 0,
                         @Embedded var location: Location = Location()): Parcelable
                       /*  var lat : Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f): Parcelable

                        */

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable