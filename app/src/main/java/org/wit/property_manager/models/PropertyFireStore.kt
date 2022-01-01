package org.wit.property_manager.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.property_manager.helpers.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File


class PropertyFireStore(val context: Context) : PropertyStore {
    val properties = ArrayList<PropertyModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override suspend fun findAll(): List<PropertyModel> {
        return properties
    }

    override suspend fun findById(id: Long): PropertyModel? {
        val foundProperty: PropertyModel? = properties.find { p -> p.id == id }
        return foundProperty
    }

    override suspend fun create(property: PropertyModel) {
        val key = db.child("users").child(userId).child("properties").push().key
        key?.let {
            property.fbId = key
            properties.add(property)
            db.child("users").child(userId).child("properties").child(key).setValue(property)
            updateImage(property)
        }
    }
    override suspend fun findByFbId(id: String): PropertyModel? {
        val foundProperty: PropertyModel? = properties.find { p -> p.fbId === id}
        return foundProperty
    }


    override suspend fun update(property: PropertyModel) {
        var foundProperty: PropertyModel? = properties.find { p -> p.fbId == property.fbId }
        if (foundProperty != null) {
            foundProperty.title = property.title
            foundProperty.description = property.description
            foundProperty.type = property.type
            foundProperty.status = property.status
            foundProperty.image = property.image
            foundProperty.location = property.location
        }

        db.child("users").child(userId).child("properties").child(property.fbId).setValue(property)
        if(property.image.length > 0){
            updateImage(property)
        }
    }

    override suspend fun delete(property: PropertyModel) {
        db.child("users").child(userId).child("properties").child(property.fbId).removeValue()
        properties.remove(property)
    }

    override suspend fun clear() {
        properties.clear()
    }

    fun fetchProperties(propertiesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(properties) {
                    it.getValue<PropertyModel>(
                        PropertyModel::class.java
                    )
                }
                propertiesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://propertymanager-gh-default-rtdb.firebaseio.com/").reference
        properties.clear()
        db.child("users").child(userId).child("properties")
            .addListenerForSingleValueEvent(valueEventListener)
    }
    fun updateImage(property: PropertyModel){
        if(property.image != ""){
            val fileName = File(property.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, property.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)

                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        property.image = it.toString()
                        db.child("users").child(userId).child("properties").child(property.fbId).setValue(property)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }

        }
    }
}


