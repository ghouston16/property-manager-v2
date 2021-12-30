package org.wit.property_manager.views.propertylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.wit.property_manager.views.propertylist.PropertyListener
import org.wit.property_manager.databinding.ActivityPropertyListBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.R
import org.wit.property_manager.views.propertylist.PropertyAdapter
import org.wit.property_manager.models.PropertyModel
import timber.log.Timber.i

class PropertyListView : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityPropertyListBinding
    lateinit var presenter: PropertyListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = PropertyListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()
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
            R.id.item_logout -> { presenter.doLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        presenter.doEditProperty(property)

    }

    private fun updateRecyclerView(){
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                PropertyAdapter(presenter.getProperties(), this@PropertyListView)
        }
    }

}