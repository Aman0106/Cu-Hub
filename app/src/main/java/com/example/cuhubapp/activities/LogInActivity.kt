package com.example.cuhubapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuhubapp.R
import com.google.firebase.firestore.FirebaseFirestore

class LogInActivity : AppCompatActivity() {
    
    private lateinit var db: FirebaseFirestore
    private lateinit var edtUserId: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignIn: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()
        initializeVariables()
        
        btnSignIn.setOnClickListener { 
            val userId = edtUserId.text.toString()
            val userPass = edtPassword.text.toString()
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
    
    private fun initializeVariables(){
        db = FirebaseFirestore.getInstance()
        edtPassword = findViewById(R.id.edt_password)
        edtUserId = findViewById(R.id.edt_user_id)
        btnSignIn = findViewById(R.id.btn_sign_in)
    }
}