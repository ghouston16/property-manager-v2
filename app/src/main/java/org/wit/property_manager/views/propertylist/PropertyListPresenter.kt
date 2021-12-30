package org.wit.property_manager.views.propertylist
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//import org.wit.property_manager.activities.PropertyMapsActivity
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.views.login.LoginView
import org.wit.property_manager.views.map.PropertyMapsView
import org.wit.property_manager.views.property.PropertyView

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
        val launcherIntent = Intent(view, PropertyView::class.java)
        launcherIntent.putExtra("property_edit", property)
        editIntentLauncher.launch(launcherIntent)
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