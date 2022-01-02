package org.wit.property_manager.views.propertylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.property_manager.databinding.CardPropertyBinding
import org.wit.property_manager.models.PropertyModel

interface PropertyListener {
    fun onPropertyClick(property: PropertyModel)
    fun onFavouriteClick(property: PropertyModel, favourite: Boolean)
}

class PropertyAdapter constructor(
    private var properties: ArrayList<PropertyModel>,
    private val listener: PropertyListener,
) :
    RecyclerView.Adapter<PropertyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPropertyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val property = properties[holder.adapterPosition]
        holder.bind(property, listener)
    }

    override fun getItemCount(): Int = properties.size

    class MainHolder(private val binding: CardPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(property: PropertyModel, listener: PropertyListener) {
            var favourite = property.favourite
            if(favourite)
            {
                binding.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            }
            binding.root.tag = property.fbId
            binding.propertyTitle.text = property.title
            binding.description.text = property.description
            if (property.image != "") {
                Picasso.get()
                    .load(property.image)
                    .resize(200, 200)
                    .into(binding.imageIcon)
            }
            binding.root.setOnClickListener { listener.onPropertyClick(property) }
            binding.favouriteButton.setOnClickListener {
                if (favourite == true) {
                    binding.favouriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    favourite = false;
                    listener.onFavouriteClick(property, favourite)
                } else {
                    binding.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    favourite = true;
                    listener.onFavouriteClick(property, favourite)
                }
            }
        }
    }
}
