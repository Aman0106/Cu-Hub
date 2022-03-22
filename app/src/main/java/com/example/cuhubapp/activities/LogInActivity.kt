package com.example.cuhubapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityLogInBinding
import com.example.cuhubapp.databinding.FragmentNewChatBinding
import com.google.firebase.firestore.FirebaseFirestore

class LogInActivity : AppCompatActivity() {
    
    private lateinit var db: FirebaseFirestore
    private lateinit var edtUserId: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignIn: Button

    private lateinit var binding: ActivityLogInBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()


        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        binding.btnSignIn.setOnClickListener {
            val userId = binding.edtUserId.text.toString()
            val userPass = binding.edtPassword.text.toString()
            val usr = db.collection("users").document(userId)

            usr.get()
                .addOnSuccessListener {
                    if(it.data!=null){
                        if (it.getString("password") == userPass){
                            val name = it.getString("name")
                            val course = it.getString("course")
                            val sec = it.getLong("section") //as Int?
                            val grp = it.getString("group")
                            val yer = it.getLong("year") //as Int?

                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("user", User(userId, name, course, sec, grp, yer))
                            }
                            startActivity(intent)
                        }
                        else
                            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this, "Invalid User Id", Toast.LENGTH_SHORT).show()
                }
        }
    }

}