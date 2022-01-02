package org.wit.property_manager.room


import android.content.Context
import androidx.room.Room
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.models.PropertyStore

class PropertyStoreRoom(val context: Context) : PropertyStore {

    var dao: PropertyDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.propertyDao()
    }

    override suspend fun findAll(): List<PropertyModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): PropertyModel? {
        return dao.findById(id)
    }

    override suspend fun findByFbId(id: String): PropertyModel? {
        return null
    }

    override suspend fun create(property: PropertyModel) {
        dao.create(property)
    }

    override suspend fun update(property: PropertyModel) {
        dao.update(property)
    }

    override suspend fun delete(property: PropertyModel) {
        dao.deleteProperty(property)
    }

    override suspend fun clear(){

    }
    override suspend fun setFavourite(property: PropertyModel) {
        dao.setFavourite(property)
        }

    override suspend fun getFavourites(): List<PropertyModel> {
        return dao.getFavourites()
    }
}