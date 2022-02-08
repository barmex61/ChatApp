package com.fatih.simplechat.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.fatih.simplechat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignIn : Fragment() {
    private var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()
        button_login.setOnClickListener {
            logIn(it)
        }
        button_signup.setOnClickListener {
            signUp(it)
        }
    }
    private fun logIn(view: View){
        if(emailText.text.isNotEmpty()&&passwordText.text.isNotEmpty()){
            auth!!.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnFailureListener {
                it.printStackTrace()
            }.addOnSuccessListener {
                val action= SignInDirections.actionSignInToMainFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(requireContext(),"Lütfen Bilgileri Eksiksiz Doldurunuz...", Toast.LENGTH_LONG).show()
        }
    }
    private fun signUp(view:View){
        val action=SignInDirections.actionSignInToSignUp()
        Navigation.findNavController(view).navigate(action)
    }

}