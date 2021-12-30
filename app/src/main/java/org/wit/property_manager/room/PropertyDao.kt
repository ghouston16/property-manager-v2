package org.wit.property_manager.room
import androidx.room.*
import org.wit.property_manager.models.PropertyModel

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(property: PropertyModel)

    @Query("SELECT * FROM PropertyModel")
    suspend fun findAll(): List<PropertyModel>

    @Query("select * from PropertyModel where id = :id")
    suspend fun findById(id: Long): PropertyModel

    @Update
    suspend fun update(property: PropertyModel)

    @Delete
    suspend fun deleteProperty(property: PropertyModel)
}