package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityFacultyMainBinding
import com.example.cuhubapp.databinding.ActivityMainBinding
import com.example.cuhubapp.fragments.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FacultyMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacultyMainBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var curUser: FacultyUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_main)
        initializeValues()

        Toast.makeText(this, "Welcome ${curUser.name}", Toast.LENGTH_SHORT).show()

        binding.bottomNavigation.setOnItemSelectedListener{

            when(it.itemId){
                R.id.menu_chats-> {
                    replaceFragment(ChatsFragment())
                }
                R.id.menu_classes-> {
                    replaceFragment(SyllabusFragment())
                }
                R.id.menu_profile-> {
                    replaceFragment(FacultyProfileFragment())
                }
            }

            return@setOnItemSelectedListener true
        }

//        getUserList()

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putParcelable("facultyUser", curUser)
        bundle.putString("uid", curUser.uid)
        bundle.putSerializable("userType", UserType.FACULTY)
        fragment.arguments = bundle

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun initializeValues() {
        firestore = FirebaseFirestore.getInstance()
        binding = ActivityFacultyMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        curUser = FacultyUser()

        curUser = intent.getParcelableExtra("facultyUser")!!
        replaceFragment(ChatsFragment())
    }
}