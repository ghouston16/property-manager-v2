
package org.wit.property_manager.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.property_manager.R
import org.wit.property_manager.activities.UserActivity
import org.wit.property_manager.adapters.UserAdapter
import org.wit.property_manager.adapters.UserListener
import org.wit.property_manager.databinding.ActivityUserListBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.views.propertylist.PropertyListView
import timber.log.Timber.i

class UserListActivity : AppCompatActivity(), UserListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityUserListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    val user = UserModel()
    var currentUser = UserModel()
    var isAdmin = true
   // val admin = UserModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        if (intent.hasExtra("current_user")){
            currentUser = intent.extras?.getParcelable<UserModel>("current_user")!!
            i("$currentUser")
            }
        loadUsers()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menu.getItem(3).isVisible= true
        menuInflater.inflate(R.menu.menu_admin, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, UserActivity::class.java)
                launcherIntent.putExtra("current_user", currentUser)
                startActivityForResult(launcherIntent,0)
            }
        }
        when (item.itemId) {
            R.id.item_properties -> {
                val launcherIntent = Intent(this, PropertyListView::class.java)
                launcherIntent.putExtra("current_user", currentUser).putExtra("user", currentUser)
                startActivityForResult(launcherIntent,0)
            }
        }
        when (item.itemId) {
            R.id.item_deleteAll -> {
                app.users.deleteAll()
                Toast
                    .makeText(
                        app.applicationContext,
                        "Deleting User Accounts",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                val launcherIntent = Intent(this, UserListActivity::class.java)
                launcherIntent.putExtra("current_user", currentUser).putExtra("user", currentUser)
                startActivityForResult(launcherIntent,0)
            }
    }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserClick(user: UserModel) {
        val launcherIntent = Intent(this, UserActivity::class.java)
        launcherIntent.putExtra("user_edit", user).putExtra("current_user",currentUser)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadUsers() }
    }

    fun loadUsers() {
        showUsers(app.users.findAll())
    }

    fun showUsers (users: List<UserModel>) {
        binding.recyclerView.adapter = UserAdapter(users, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}