package org.wit.property_manager.views.property

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyBinding
import org.wit.property_manager.models.PropertyModel
import timber.log.Timber.i

class PropertyView : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    private lateinit var presenter: PropertyPresenter
    lateinit var map: GoogleMap
    var property = PropertyModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PropertyPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheProperty(
                binding.propertyTitle.text.toString(),
                binding.description.text.toString(),
                binding.propertyType.text.toString(),
                binding.propertyStatus.text.toString()
            )
            presenter.doSelectImage()
        }
/*
        binding.propertyLocation.setOnClickListener {
            presenter.cacheProperty(binding.propertyTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }
*/
        binding.mapView2.setOnClickListener {
            presenter.cacheProperty(
                binding.propertyTitle.text.toString(),
                binding.description.text.toString(),
                binding.propertyType.text.toString(),
                binding.propertyStatus.text.toString()
            )
            presenter.doSetLocation()
        }

        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit) {
            deleteMenu.setVisible(true)
        } else {
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.propertyTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_property_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doAddOrSave(
                            binding.propertyTitle.text.toString(),
                            binding.description.text.toString(),
                            binding.propertyType.text.toString(),
                            binding.propertyStatus.text.toString()
                        )
                    }
                }
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete()
                }
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showProperty(property: PropertyModel) {
        if (binding.propertyTitle.text.isEmpty()) binding.propertyTitle.setText(property.title)
        if (binding.description.text.isEmpty()) binding.description.setText(property.description)
        if (binding.propertyType.text.isEmpty()) binding.propertyType.setText(property.type)
        if (binding.propertyStatus.text.isEmpty()) binding.propertyStatus.setText(property.status)
        Picasso.get()
            .load(property.image)
            .into(binding.propertyImage)

        if (property.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_property_image)
        }
        binding.lat.setText("%.6f".format(property.location.lat))
        binding.lng.setText("%.6f".format(property.location.lng))

    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.propertyImage)
        binding.chooseImage.setText(R.string.change_property_image)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }

}