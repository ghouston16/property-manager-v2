package org.wit.property_manager.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.property_manager.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_property_image.toString())
    intentLauncher.launch(chooseFile)
}