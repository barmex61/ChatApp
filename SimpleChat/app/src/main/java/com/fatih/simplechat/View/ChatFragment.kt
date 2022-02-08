package com.fatih.simplechat.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.simplechat.Adapter.MessageAdapter
import com.fatih.simplechat.Model.Message
import com.fatih.simplechat.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {
    var name:String?=null
    var recieverUid:String?=null
    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var firestore: FirebaseFirestore
    var recieverRoom:String?=null
    var senderRoom:String?=null
    var senderUid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList= ArrayList()
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        messageAdapter= MessageAdapter(messageList)
        recyclerView.adapter=messageAdapter

        firestore= FirebaseFirestore.getInstance()
        if(arguments!=null){
            arguments?.let {
                name=ChatFragmentArgs.fromBundle(it ).name
                recieverUid=ChatFragmentArgs.fromBundle(it).userId
                senderUid=FirebaseAuth.getInstance().currentUser?.uid
                senderRoom=recieverUid+senderUid
                recieverRoom=senderUid+recieverUid
            }
        }
        (activity as AppCompatActivity?)!!.supportActionBar!!.title=name

        firestore.collection("Chat").document(senderRoom!!).collection("Messages").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(requireContext(),error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
               if(value!=null){

                   messageList.clear()
                   for(data in value.documents){

                       val messages=data.toObject<Message>()
                       if(messages!=null){
                           messageList.add(messages)
                       }
                   }
                   messageAdapter.notifyDataSetChanged()
               }
            }

        }
        apply.setOnClickListener {
            val uuid=UUID.randomUUID()
            val message=messageBox.text.toString()
            val messageObject=Message(message,senderUid,Timestamp.now())
            firestore.collection("Chat").document(senderRoom!!).collection("Messages").document(uuid.toString()).set(messageObject)
                .addOnSuccessListener {
                    val uuidi=UUID.randomUUID()
                    firestore.collection("Chat").document(recieverRoom!!).collection("Messages").document(uuidi.toString()).set(messageObject)

                }
            messageBox.setText("")
        }

    }
}