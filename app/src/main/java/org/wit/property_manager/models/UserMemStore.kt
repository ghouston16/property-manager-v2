package org.wit.property_manager.models

import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.models.UserStore
import timber.log.Timber.i

var lastUserId = 0L

internal fun getUserId(): Long {
    return lastUserId++
}

// todo user - json store
class UserMemStore: UserStore {
    val users = ArrayList<UserModel>()
    var admin = mutableListOf<String>("gh@wit.ie")

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        user.id = getUserId()
        users.add(user)
        logAll()
    }
    override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { p -> p.id == user.id }
    if (foundUser != null) {
        foundUser.email = user.email
        foundUser.password = user.password
        foundUser.image = user.image
        foundUser.lat = user.lat
        foundUser.lng = user.lng
        foundUser.zoom = user.zoom
        logAll()
    }
}
    override fun delete(user: UserModel) {
        users.remove(user)
    }

    override fun deleteAll() {
        users.clear()
    }
    fun logAll() {
        users.forEach{ i("${it}") }
    }
}