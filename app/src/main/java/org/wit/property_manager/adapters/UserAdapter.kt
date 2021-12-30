package org.wit.property_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.property_manager.databinding.CardUserBinding
import org.wit.property_manager.models.UserModel


interface UserListener {
    fun onUserClick(user: UserModel)
}
class UserAdapter constructor(private var users: List<UserModel>,
                                  private val listener: UserListener) :
    RecyclerView.Adapter<UserAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user = users[holder.adapterPosition]
        holder.bind(user, listener)
    }

    override fun getItemCount(): Int = users.size

    class MainHolder(private val binding : CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserModel, listener: UserListener) {
            binding.userEmail.text = user.email

            Picasso.get().load(user.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onUserClick(user) }
        }
    }
}