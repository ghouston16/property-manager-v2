package org.wit.property_manager.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    var id: Long = 0, var email: String = "",
    var password: String = "", var image: Uri = Uri.EMPTY, var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable {

}
