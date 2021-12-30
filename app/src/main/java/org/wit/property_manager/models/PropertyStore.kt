package org.wit.property_manager.models

interface PropertyStore {
    fun findAll(): List<PropertyModel>
    //fun findAll(id: Long): List<PropertyModel>
    fun findById(id:Long) : PropertyModel?
    fun create(property: PropertyModel)
    fun update(property: PropertyModel)
    fun delete(property: PropertyModel)
   // fun deleteByUser(id: Long)
    fun deleteAll()
}