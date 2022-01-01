package org.wit.property_manager.views.propertylist
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
//import org.wit.property_manager.activities.PropertyMapsActivity
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.views.login.LoginView
import org.wit.property_manager.views.map.PropertyMapsView
import org.wit.property_manager.views.property.PropertyView
import timber.log.Timber.i

class PropertyListPresenter(private val view: PropertyListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getProperties() = app.properties.findAll()

    fun doAddProperty() {
        val launcherIntent = Intent(view, PropertyView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditProperty(property: PropertyModel) {
            i("$property")
            val launcherIntent = Intent(view, PropertyView::class.java)
            launcherIntent.putExtra("property_edit", property)
            editIntentLauncher.launch(launcherIntent)
    }
    fun doFavourite(property: PropertyModel, favourite: Boolean) {
        property.favourite = favourite
        GlobalScope.launch(Dispatchers.Main) {
            app.properties.setFavourite(property)
        }
    }

    suspend fun doDeleteProperty(id: String) {
        GlobalScope.launch(Dispatchers.Main) {
            i("Deleting Property")
            val property = app.properties.findByFbId(id)
            if (property != null) app.properties.delete(property)
            getProperties()

        }
    }
    fun doSwipeEdit(id: String){
        GlobalScope.launch(Dispatchers.Main) {
            i("Getting Object $id from DB")
            val property = app.properties.findByFbId(id)
            if (property != null) {
                i("Open Editor: $property")
                doEditProperty(property)
            }
        }
    }


        fun doShowPropertiesMap() {
        val launcherIntent = Intent(view, PropertyMapsView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }


    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.properties.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getProperties()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}