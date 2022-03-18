package com.example.cuhubapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuhubapp.R
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
            Toast.makeText(this, "hoi", Toast.LENGTH_SHORT).show()
            val userId = binding.edtUserId.text.toString()
            val userPass = binding.edtPassword.text.toString()
            Toast.makeText(this, "$userId and this is pass $userPass", Toast.LENGTH_SHORT).show()
            val usr = db.collection("users").document(userId)

            usr.get()
                .addOnSuccessListener {
                    if(it.data!=null){
                        if (it.getString("password") == userPass){
                            startActivity(Intent(this, MainActivity::class.java))
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