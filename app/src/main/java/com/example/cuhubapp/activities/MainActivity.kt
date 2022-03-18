package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.databinding.ActivityMainBinding
import com.example.cuhubapp.fragments.ChatsFragment
import com.example.cuhubapp.fragments.NewChatFragment
import com.example.cuhubapp.fragments.ProfileFragment
import com.example.cuhubapp.fragments.SyllabusFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        replaceFragment(ChatsFragment())

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

    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}