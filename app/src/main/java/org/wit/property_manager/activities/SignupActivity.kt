package org.wit.property_manager.activities
/*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.property_manager.databinding.ActivitySignupBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.views.propertylist.PropertyListView
import timber.log.Timber
import timber.log.Timber.i

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var loginIntent: Intent
    var user = UserModel()
    var candidate = UserModel()
    var emailValid = false
    var passwordValid = false

    //   val users = UserMemStore()
    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        val admin = "gh@wit.ie"
       // val userList = app.users.findAll()
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.plant(Timber.DebugTree())

        i("Signup Activity started...")

        binding.btnSignup.setOnClickListener() {
            val emailList = app.users.findAll()
            var emailInUse = false
            user.email = binding.userEmail.text.toString()
            user.password = binding.userPassword.text.toString()
            for (i in emailList)
            {
                if (user.email == i.email){
                    i("match found")
                    emailInUse = true
                } else {
                    i("match not found")
                }
            }
            if (user.email.isNotEmpty() && user.password.isNotEmpty() && !emailInUse) {
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
                        if (user.password.length >= 8) {
                            passwordValid = true
                        }
                        if (passwordValid && emailValid) {
                            app.users.create(user.copy())
                            Snackbar
                                .make(
                                    it,
                                    "Registration successful - Please log in..",
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                        } else {
                            Snackbar
                                .make(
                                    it,
                                    "Please Enter a Valid Email and 8+ char Password",
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                        }
                    } else {
                        Snackbar
                            .make(it, "Email/Password Invalid or Email in Use", Snackbar.LENGTH_LONG)
                            .show()

            }
        }
        // Login Method and validation
        binding.btnLogin.setOnClickListener() {
            candidate.email = binding.userEmail.text.toString()
            candidate.password = binding.userPassword.text.toString()
            val userList = app.users.findAll()
            if (candidate.email.isNotEmpty() && candidate.password.isNotEmpty()) {
                for (person in userList) {
                    if (candidate.email == person.email && candidate.password == person.password) {
                        i("User Logged In $person")
                        if (candidate.email == admin) {
                            val launcherIntent = Intent(this, UserListActivity::class.java)
                            launcherIntent.putExtra("user", person).putExtra("current_user", person)
                            startActivityForResult(launcherIntent, 0)
                        } else {
                            val launcherIntent = Intent(this, PropertyListView::class.java)
                            launcherIntent.putExtra("user", person).putExtra("current_user", person)
                            i("$person")
                            startActivityForResult(launcherIntent, 0)
                        }
                    } else {
                        Snackbar
                            .make(
                                it,
                                "Please Enter a valid Email and Password",
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                }
            } else {
                Snackbar
                    .make(it, "Please Enter Both an Email and Password", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}

 */
