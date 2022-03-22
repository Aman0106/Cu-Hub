package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityMainBinding
import com.example.cuhubapp.fragments.ChatsFragment
import com.example.cuhubapp.fragments.NewChatFragment
import com.example.cuhubapp.fragments.ProfileFragment
import com.example.cuhubapp.fragments.SyllabusFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firestoreDb:FirebaseFirestore

    private lateinit var usersInSection:ArrayList<User>
    private lateinit var usersInGroup:ArrayList<User>
    private lateinit var curUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firestoreDb = FirebaseFirestore.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        curUser = intent.getParcelableExtra("user")!!
        Toast.makeText(this, curUser.uid, Toast.LENGTH_SHORT).show()

        binding.bottomNavigation.setOnItemSelectedListener{

            when(it.itemId){
                R.id.menu_chats-> {
                    replaceFragment(ChatsFragment())
                }
                R.id.menu_new_chat-> {
                    replaceFragment(NewChatFragment())
                }
                R.id.menu_syllabus-> {
                    replaceFragment(SyllabusFragment())
                }
                R.id.menu_profile-> {
                    replaceFragment(ProfileFragment())
                }
            }

            return@setOnItemSelectedListener true
        }

        getUserList()

    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putParcelable("user", curUser)
        bundle.putParcelableArrayList("usersInSection",usersInSection)
        fragment.arguments = bundle

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun getUserList():ArrayList<User>{
        usersInSection = ArrayList()
        firestoreDb.collection("users").document(curUser.uid!!)
            .get().addOnSuccessListener {
                Toast.makeText(this, "${it.getString("name")}", Toast.LENGTH_SHORT).show()
            }
        return usersInSection
    }

    private fun setUser(usr: QueryDocumentSnapshot):User{
        Toast.makeText(this, "run", Toast.LENGTH_SHORT).show()
        val uid = usr.id
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")

        val user = User(uid,name,course,sec,grp,yer)

        return user
    }
}