package com.fatih.simplechat.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fatih.simplechat.Model.User
import com.fatih.simplechat.View.MainFragmentDirections
import com.fatih.simplechat.databinding.RecyclerRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class UserAdapter(private val userList:ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserHolder>() {
    val color= arrayListOf("#334455","#e390a1","#a18e33","#6455ab","#119083","#5587ea","#eeabc0","#7760ba")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
            val currentUser=userList[position]
            holder.binding.textView.text=currentUser.name
            if(userList[position].image!=null){
                Picasso.get().load(userList[position].image).into(holder.binding.profileImage)
            }

            holder.binding.textView.setTextColor(Color.parseColor(color[position%8]))

            holder.itemView.setOnClickListener {
                val action=MainFragmentDirections.actionMainFragmentToChatFragment(currentUser.uid, currentUser.name)
                Navigation.findNavController(it).navigate(action)
            }
    }

    override fun getItemCount(): Int {
            return userList.size
    }
    class UserHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}