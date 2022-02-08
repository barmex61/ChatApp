package com.fatih.simplechat.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fatih.simplechat.Model.Message
import com.fatih.simplechat.databinding.RecieveBinding
import com.fatih.simplechat.databinding.RecyclerRowBinding
import com.fatih.simplechat.databinding.SentBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val messageList:ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE=1
    val ITEM_SENT=2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            val binding=RecieveBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return RecieveViewHolder(binding)
        }else{
            val binding=SentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return SentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass==SentViewHolder::class.java){
                val viewHolder=holder as SentViewHolder
                val currentMessage=messageList[position]
                holder.binding.sentMessage.text=currentMessage.message
        }else{
                val viewHolder=holder as RecieveViewHolder
                val currentMessage=messageList[position]
                holder.binding.recieveMessage.text=currentMessage.message

        }
    }

    override fun getItemCount(): Int {
            return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        var currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECIEVE
        }
    }
    class SentViewHolder(val binding:SentBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    class RecieveViewHolder(val binding:RecieveBinding) :RecyclerView.ViewHolder(binding.root){

    }

}