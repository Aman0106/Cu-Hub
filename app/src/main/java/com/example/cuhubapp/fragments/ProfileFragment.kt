package com.example.cuhubapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.ImageViewActivity
import com.example.cuhubapp.activities.LogInActivity
import com.example.cuhubapp.activities.StudentListActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var curUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeValues()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardTimeTable.setOnClickListener {
            val timeTableName:String = curUser.yer.toString() + "_" + curUser.course + "_" + curUser.section.toString() + ".png"
            val intent = Intent(view.context, ImageViewActivity::class.java).apply {
                putExtra("image","/time_tables/$timeTableName")
            }
            startActivity(intent)
        }
        binding.cardSection.setOnClickListener {
            val intent = Intent(view.context, StudentListActivity::class.java).apply {
//                putExtra("showAll", true)
                putExtra("curUser", curUser)
                putExtra("listType", ListType.SECTION)
            }
            startActivity(intent)
        }
        binding.cardGroup.setOnClickListener {
            val intent = Intent(view.context, StudentListActivity::class.java).apply {
//                putExtra("showAll", false)
                putExtra("curUser", curUser)
                putExtra("listType", ListType.GROUP)
            }
            startActivity(intent)
        }

        binding.cardFaculty.setOnClickListener {
            val intent = Intent(activity, StudentListActivity::class.java).apply {
                putExtra("listType", ListType.FACULTY)
                putExtra("curUser", curUser)
            }
            startActivity(intent)
        }

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
                startActivity(Intent(activity,LogInActivity::class.java))
                activity?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeValues(){
        setHasOptionsMenu(true)

        firebaseStorage = Firebase.storage

        val bundle = arguments
        curUser = bundle?.getParcelable("user")!!
    }
}

enum class ListType{
    FACULTY, GROUP, SECTION
}