package org.wit.property_manager.models

interface PropertyStore {
    suspend fun findAll(): List<PropertyModel>
    //fun findAll(id: Long): List<PropertyModel>
    suspend fun findById(id:Long) : PropertyModel?
    suspend fun create(property: PropertyModel)
    suspend fun update(property: PropertyModel)
    suspend fun delete(property: PropertyModel)
   // fun deleteByUser(id: Long)
   // suspend fun deleteAll()
    suspend fun clear()
    //fun fetchProperties(): List<PropertyModel>
}