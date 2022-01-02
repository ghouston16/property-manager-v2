package org.wit.property_manager.activities
/*
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout.make
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.squareup.picasso.Picasso
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityUserBinding
import org.wit.property_manager.helpers.showImagePicker
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.models.Location
import org.wit.property_manager.views.location.EditLocationView
import org.wit.property_manager.views.propertylist.PropertyListView
import timber.log.Timber
import timber.log.Timber.i


class UserActivity : AppCompatActivity() {
    // register callback for image picker
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityUserBinding
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    var emailValid = true
    var passwordValid = true

    var user = UserModel()
    var edit = false
    var currentUser = UserModel()
    var isAdmin = false
    val admin = mutableListOf<String>("gh@wit.ie")

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        var edit = false
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        if (intent.hasExtra("current_user")) {
            currentUser = intent.extras?.getParcelable("current_user")!!
            i("Current USER:$currentUser")
        }
        if (currentUser.email ==admin[0]){
            isAdmin =true
        }
        app = application as MainApp

        //   Timber.plant(Timber.DebugTree())
        i("User Activity started...")
        if (intent.hasExtra("user_edit")) {
            edit = true
            user = intent.extras?.getParcelable<UserModel>("user_edit")!!
            binding.userEmail.setText(user.email)
            binding.userPassword.setText(user.password)
            i("$user")
            Picasso.get()
                .load(user.image)
                .into(binding.userImage)
            if (user.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_property_image)
            }
            if (edit) {
                binding.btnAdd.setText(R.string.button_updateUser)
            }
        }

        binding.btnAdd.setOnClickListener() {
            user.email = binding.userEmail.text.toString()
            user.password = binding.userPassword.text.toString()
            if (user.email.isNotEmpty() && user.password.isNotEmpty()) {
                passwordValid = user.password.length >= 8
                var count = (user.email.length - 1)
                for (i in 0..count) {
                    emailValid = if (user.email.contains("@")) {
                        i("Valid email entered")
                        true
                    } else {
                        i("Email Invalid")
                        false
                    }
                }
                if (passwordValid && emailValid) {
                    if (edit) {
                        if (intent.hasExtra("location")) {
                            var location = intent.extras?.getParcelable<Location>("location")!!
                            user.lng = location.lng
                            user.lat = location.lat
                            user.zoom = location.zoom
                        }
                        app.users.update(user.copy())
                        Toast
                            .makeText(
                                app.applicationContext,
                                "User Updated Successfully",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        if (isAdmin) {
                            val launcherIntent = Intent(this, UserListActivity::class.java)
                            launcherIntent.putExtra("current_user", currentUser).putExtra("user", currentUser)
                            // remove log
                            i("$currentUser")
                            startActivityForResult(launcherIntent, 0)
                        } else {
                            val launcherIntent = Intent(this, PropertyListView::class.java)
                            launcherIntent.putExtra("current_user", currentUser).putExtra("user",currentUser)
                            startActivityForResult(launcherIntent, 0)
                        }
                    } else {
                        binding.btnAdd.setText(R.string.button_addUser)
                        app.users.create(user.copy())
                        val launcherIntent = Intent(this, UserListActivity::class.java)
                        launcherIntent.putExtra("current_user", currentUser).putExtra("user",currentUser)
                        startActivityForResult(launcherIntent, 0)
                    }
                } else {
                    Snackbar
                        .make(it, "Please enter a valid Email & Password", Snackbar.LENGTH_LONG)
                        .show()

                }
            }
        }
        binding.userLocation.setOnClickListener {
            var location = Location(52.245696, -7.139102, 15f)
            if (user.zoom != 0f) {
                location.lat = user.lat
                location.lng = user.lng
                location.zoom = user.zoom
            }
            val launcherIntent = Intent(this, EditLocationView::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        binding.btnDelete.setOnClickListener {
            app.users.delete(user)
            if (isAdmin){
                val launcherIntent = Intent(this, UserListActivity::class.java)
                launcherIntent.putExtra("current_user",currentUser)
                startActivityForResult(launcherIntent, 0)
            } else {
                val launcherIntent = Intent(this, SignupActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
            Toast
                .makeText(
                    app.applicationContext,
                    "User Deleted",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        /*
        when (item.itemId) {
            R.id.item_settings -> {
                var user = UserModel()
                user = intent.extras?.getParcelable("user")!!
                val launcherIntent = Intent(this, UserActivity::class.java)
                    launcherIntent.putExtra("user_edit", user).putExtra("current_user", currentUser)
                startActivityForResult(launcherIntent, 0)
            }
        }

         */
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            // image equals returned image
                            user.image = result.data!!.data!!
                            // use to get image to display
                            Picasso.get()
                                .load(user.image)
                                .into(binding.userImage)
                            binding.chooseImage.setText(R.string.add_property_image)
                            if (user.image != Uri.EMPTY) {
                                binding.chooseImage.setText(R.string.change_property_image)
                            }
                        } // end of if
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            user.lat = location.lat
                            user.lng = location.lng
                            user.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }
}


 */