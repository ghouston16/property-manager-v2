package org.wit.property_manager.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyFireStore

import org.wit.property_manager.views.propertylist.PropertyListView


class LoginPresenter(val view: LoginView) {
    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: PropertyFireStore? = null

    init {
        registerLoginCallback()
        if (app.properties is PropertyFireStore) {
            fireStore = app.properties as PropertyFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchProperties {
                        view?.hideProgress()
                        val launcherIntent = Intent(view, PropertyListView::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                } else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, PropertyListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view?.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }

    }

    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchProperties {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, PropertyListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
        } else {
            view.showSnackBar("Login failed: ${task.exception?.message}")
        }
        view.hideProgress()
    }
}

private fun registerLoginCallback() {
    loginIntentLauncher =
        view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { }
}
}