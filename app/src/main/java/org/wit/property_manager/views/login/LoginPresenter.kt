package org.wit.property_manager.views.login

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.wit.property_manager.R
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyFireStore

import org.wit.property_manager.views.propertylist.PropertyListView
import timber.log.Timber


class LoginPresenter(val view: LoginView) {
    private lateinit var loginIntentLauncher: ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: PropertyFireStore? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var errorStatus = MutableLiveData<Boolean>()
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    // Google Auth
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    init {
        registerLoginCallback()
        if (app.properties is PropertyFireStore) {
            fireStore = app.properties as PropertyFireStore
        }
        configureGoogleSignIn()
        setupGoogleSignInCallback()
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

    fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient.value = GoogleSignIn.getClient(app!!.applicationContext, gso)


    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.i("Property firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(app!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    if (fireStore != null) {
                        fireStore!!.fetchProperties {
                            view?.hideProgress()
                            val launcherIntent = Intent(view, PropertyListView::class.java)
                            loginIntentLauncher.launch(launcherIntent)
                        }

                    }
                }else {
                        // If sign in fails, display a message to the user.
                        Timber.i("signInWithCredential:failure $task.exception")
                        view.showSnackBar("Login failed: ${task.exception?.message}")
                    }
                }
            }

        fun googleSignIn() {
            val signInIntent = googleSignInClient.value!!.signInIntent

            startForResult.launch(signInIntent)
        }

        private fun setupGoogleSignInCallback() {
            startForResult =
                view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    when (result.resultCode) {
                        RESULT_OK -> {
                            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                            try {
                                // Google Sign In was successful, authenticate with Firebase
                                val account = task.getResult(ApiException::class.java)
                                view.authWithGoogle(account!!)
                            } catch (e: ApiException) {
                                // Google Sign In failed
                                Timber.i("Google sign in failed $e")
                                view.showSnackBar("Login failed: ${task.exception?.message}")
                            }
                            Timber.i("DonationX Google Result $result.data")
                        }
                        RESULT_CANCELED -> {

                        }
                        else -> {}
                    }
                }
        }
    }