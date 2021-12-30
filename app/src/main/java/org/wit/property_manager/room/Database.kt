package org.wit.property_manager.room


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.property_manager.helpers.Converters
import org.wit.property_manager.models.PropertyModel


@Database(entities = arrayOf(PropertyModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
}