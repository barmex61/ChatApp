package com.fatih.simplechat.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.fatih.simplechat.Model.User
import com.fatih.simplechat.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_edit.*
import java.util.*


class EditFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedUri: Uri?=null
    private lateinit var firestore:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var reference: StorageReference
    private lateinit var storage:FirebaseStorage
    private var downloadedUri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        reference=storage.reference
        registerLauncher()
        imageView.setOnClickListener {
            selectImage(it)
        }
        saveButton.setOnClickListener {
            save(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
    private fun selectImage(view:View){
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Need Permission",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
    }
    private fun save(view:View){
        if(selectedUri!=null){
            val uid=auth.currentUser!!.uid
            val uuid=UUID.randomUUID()
            val name="image/$uuid.png"
            reference.child(name).putFile(selectedUri!!).addOnSuccessListener {
                       storage.getReference(name).downloadUrl.addOnSuccessListener {
                           downloadedUri=it
                           firestore.collection("Users").document(uid).update("image",downloadedUri.toString(),"number",editTextNumber.text.toString(),"ad",nameText.text.toString()).addOnSuccessListener {
                               val action=EditFragmentDirections.actionEditFragmentToMainFragment()
                               Navigation.findNavController(view).navigate(action)
                           }
                       }
            }

        }
    }
    private fun registerLauncher(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }else{
                Toast.makeText(requireContext(),"Need Permission !!!",Toast.LENGTH_LONG).show()
            }
        }
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode== Activity.RESULT_OK){
                val intentFromResult=it.data
                if(intentFromResult!=null){
                    selectedUri=intentFromResult.data
                    imageView.setImageURI(selectedUri)
                }
            }
        }
    }
}