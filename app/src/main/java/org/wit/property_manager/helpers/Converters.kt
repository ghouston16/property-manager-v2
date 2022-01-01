package org.wit.property_manager.helpers

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromUri(value: Uri): String {
        return value?.toString()
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
        return Uri.parse(string)
    }
    @TypeConverter
    fun listToJson(value: ArrayList<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): ArrayList<String> {
        val array = Gson().fromJson(value, Array<String>::class.java)
        val arrayList = ArrayList(array.toMutableList())
        return arrayList
    }
}