package org.wit.property_manager.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PropertyMemStore: PropertyStore {
    val properties = ArrayList<PropertyModel>()
    val userProperties = ArrayList<PropertyModel>()

    override suspend fun findAll(): List<PropertyModel>{
        return properties
    }
    /*
    override fun findAll(id: Long): MutableList<PropertyModel> {
        for (property in properties){
            if (property.agent == id){
                userProperties.add(property)
            }
        }
        return userProperties
    }
*/


    override suspend fun create(property:PropertyModel){
        property.id = getId()
        properties.add(property)
        logAll()
    }
    override suspend fun delete(property: PropertyModel) {
        properties.remove(property)
    }
    /*
    override fun deleteByUser(id: Long) {
        for (property in properties){
            if (property.agent == id){
                properties.remove(property)
            }
        }
        // todo - find how to pass id of property to be deleted
    }

     */
    override suspend fun deleteAll(){
        logAll()
        properties.clear()
    }
    override suspend fun update(property: PropertyModel) {
        var foundProperty: PropertyModel? = properties.find { p -> p.id == property.id }
        if (foundProperty != null) {
            foundProperty.title = property.title
            foundProperty.description = property.description
            foundProperty.type = property.type
            foundProperty.status = property.status
            //foundProperty.agent = property.agent
            foundProperty.image = property.image
            foundProperty.location = property.location
            logAll()
        }
    }
    override suspend fun findById(id: Long): PropertyModel?{
        val foundProperty: PropertyModel? = properties.find { it.id == id }
        return foundProperty
}
fun logAll(){
    properties.forEach{ i("${it}")}
}
}