package org.wit.property_manager.views.propertylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.wit.property_manager.views.propertylist.PropertyListener
import org.wit.property_manager.databinding.ActivityPropertyListBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.R
import org.wit.property_manager.views.propertylist.PropertyAdapter
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.utils.SwipeToDeleteCallback
import timber.log.Timber.i

class PropertyListView : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityPropertyListBinding
    lateinit var presenter: PropertyListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityPropertyListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //update Toolbar title
            binding.toolbar.title = title
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                binding.toolbar.title = "${title}: ${user.email}"
            }
        setSupportActionBar(binding.toolbar)
        presenter = PropertyListPresenter(this)
        setSwipeRefresh()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as PropertyAdapter
                deleteProperty(viewHolder.itemView.tag as String)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerView)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        //update the view
        super.onResume()
        updateRecyclerView()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddProperty() }
            R.id.item_map -> { presenter.doShowPropertiesMap() }
            R.id.item_logout -> {
                GlobalScope.launch(Dispatchers.IO) {
                presenter.doLogout()
            }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        presenter.doEditProperty(property)

    }
    fun deleteProperty(id: String){
        i("Delete: deleteProperty")
        GlobalScope.launch(Dispatchers.Main) {
            presenter.doDeleteProperty(id)
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView(){
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                PropertyAdapter(ArrayList<PropertyModel>(presenter.getProperties()), this@PropertyListView)
        }
    }
    fun setSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                binding.swiperefresh.isRefreshing = true
                GlobalScope.launch(Dispatchers.Main) {
                    presenter.getProperties()

                }
            }
        })
    }

    fun checkSwipeRefresh() {
        if (binding.swiperefresh.isRefreshing) binding.swiperefresh.isRefreshing = false
    }
}
