package org.wit.user.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.property_manager.helpers.*
import org.wit.property_manager.helpers.exists
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.models.UserStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "users.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = mutableListOf<UserModel>()
    var admin = mutableListOf<String>("gh@wit.ie")

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }


    override fun update(user: UserModel) {
        val userList = findAll() as ArrayList<UserModel>
        var foundUser: UserModel? = userList.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            foundUser.image = user.image
            foundUser.lat = user.lat
            foundUser.lng = user.lng
            foundUser.zoom = user.zoom
        }
        serialize()
    }
    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }
    override fun deleteAll() {
        users.clear()
        serialize()
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}