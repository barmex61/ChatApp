package com.fatih.simplechat.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.fatih.simplechat.R
import com.fatih.simplechat.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*
import kotlin.collections.HashMap

class SignUp : Fragment() {
    private var auth:FirebaseAuth?=null
    private var fireStore:FirebaseFirestore?=null
    private var user:FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireStore= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        button_signup.setOnClickListener {
            signUp(it)
        }
    }
    private fun signUp(view:View){
        if(emailText.text.isNotEmpty()&&passwordText.text.isNotEmpty()&&userText.text.isNotEmpty()){
            auth!!.createUserWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnFailureListener {
                    it.printStackTrace()
            }.addOnSuccessListener {

                val userName=userText.text.toString()
                val email=emailText.text.toString()
                user= auth!!.currentUser
                val userId=user!!.uid


                 fireStore!!.collection("Users").document(userId).set( User(userName,email, userId))
                     .addOnSuccessListener {
                    val action=SignUpDirections.actionSignUpToMainFragment()
                    Navigation.findNavController(view).navigate(action)

                 }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Something went wrong !",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
               Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(requireContext(),"Lütfen Bilgileri Eksiksiz Doldurunuz...",Toast.LENGTH_LONG).show()
        }
    }
}