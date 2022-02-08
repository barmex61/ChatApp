package com.fatih.simplechat.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.simplechat.R
import com.fatih.simplechat.Model.User
import com.fatih.simplechat.Adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private lateinit var arrayList:ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title="SimpleChat"
        arrayList=ArrayList()
        auth= FirebaseAuth.getInstance()
        adapter= UserAdapter(arrayList)
        firestore= FirebaseFirestore.getInstance()
        recycler_View.layoutManager=LinearLayoutManager(requireContext())
        adapter= UserAdapter(arrayList)
        recycler_View.adapter = adapter
        firestore.collection("Users").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null&&!value.isEmpty){
                        for(document in value.documents){
                            val user=document.toObject<User>()
                            if(user!=null){
                                if(user.uid!= auth.currentUser!!.uid)
                                arrayList.add(user)
                            }
                            adapter.notifyDataSetChanged()
                        }
                }
            }
        }
        onBackPressed()
    }

    private fun onBackPressed(){
        requireActivity().fragmentManager.popBackStack();
    }
}
