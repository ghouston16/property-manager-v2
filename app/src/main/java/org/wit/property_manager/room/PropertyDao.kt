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

    @Query("select * from PropertyModel where Fbid = :id")
    suspend fun findByFbId(id: String): PropertyModel


    @Update
    suspend fun update(property: PropertyModel)

    @Delete
    suspend fun deleteProperty(property: PropertyModel)

    @Query("select * from PropertyModel where favourite = 1")
    suspend fun getFavourites(): List<PropertyModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavourite(property: PropertyModel)
}