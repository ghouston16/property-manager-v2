package org.wit.property_manager.main

import android.app.Application
import android.util.Property
import org.wit.property_manager.models.*
import org.wit.user.models.UserJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var properties: PropertyStore
    val user = UserModel()
    lateinit var users : UserStore
    //bval admin = "gh@wit.ie"

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        properties = PropertyJSONStore(applicationContext)
        users= UserJSONStore(applicationContext)
        i("Property started")
    }
}