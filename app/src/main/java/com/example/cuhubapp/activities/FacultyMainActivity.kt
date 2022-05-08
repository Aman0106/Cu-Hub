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
import com.example.cuhubapp.fragments.ChatsFragment
import com.example.cuhubapp.fragments.LoginFacultyFragment
import com.example.cuhubapp.fragments.ProfileFragment
import com.example.cuhubapp.fragments.SyllabusFragment
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
                R.id.menu_new_chat-> {
                    replaceFragment(LoginFacultyFragment())
                }
                R.id.menu_classes-> {
                    replaceFragment(SyllabusFragment())
                }
                R.id.menu_profile-> {
                    replaceFragment(ProfileFragment())
                }
            }

            return@setOnItemSelectedListener true
        }

//        getUserList()

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
//
//    private fun getUserList():ArrayList<User>{
//        usersInSection = ArrayList()
//        firestore.collection("users")
//            .whereEqualTo("course", curUser.course)
//            .whereEqualTo("year",curUser.yer)
//            .whereEqualTo("section",curUser.section)
//            .get().addOnSuccessListener {
//                for (usr in it){
//                    val user = setUser(usr)
//                    usersInSection += user
//                }
//                replaceFragment(ChatsFragment())
//            }
//        return usersInSection
//    }

    private fun setUser(usr: QueryDocumentSnapshot): User {
        val active = usr.getBoolean("active")
        val uid = usr.id
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")
        val firebaseUid = usr.getString("firebaseUid")

        return User(active, uid, firebaseUid, name, course, sec, grp, yer)
    }

    private fun initializeValues() {
        firestore = FirebaseFirestore.getInstance()
        binding = ActivityFacultyMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        curUser = FacultyUser()

        curUser = intent.getParcelableExtra("facultyuser")!!

    }
}