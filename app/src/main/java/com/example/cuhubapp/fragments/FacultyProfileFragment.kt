package com.example.cuhubapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.LogInActivity
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.databinding.FragmentFacultyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FacultyProfileFragment : Fragment() {

    private lateinit var binding:FragmentFacultyProfileBinding
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var curUser: FacultyUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeValues()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacultyProfileBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfile()
    }

    private fun setProfile(){
        binding.txtName.text = curUser.name
        binding.txtUid.text = curUser.uid
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_log_out ->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, LogInActivity::class.java))
                activity?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeValues(){
        setHasOptionsMenu(true)

        firebaseStorage = Firebase.storage

        val bundle = arguments
        curUser = bundle?.getParcelable("facultyUser")!!
    }

}