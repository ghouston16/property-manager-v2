package org.wit.property_manager.main

import android.app.Application
import android.util.Property
import org.wit.property_manager.models.*
import org.wit.property_manager.room.PropertyStoreRoom

import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var properties: PropertyStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        properties = PropertyFireStore(applicationContext)
     //   users= UserJSONStore(applicationContext)
        i("Property started")
    }
}