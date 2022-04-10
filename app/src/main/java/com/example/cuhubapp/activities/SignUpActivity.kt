package com.example.cuhubapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeValues()

        binding.btnSignIn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp(){
        val usr = firestore.collection("users")
            .whereEqualTo("email", binding.edtUserEmail.text.toString())

        usr.get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    Toast.makeText(this, "Your name is not in List", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(binding.edtUserEmail.text.toString(),
                        binding.edtPassword.text.toString())
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                val doc = it.documents[0]
                                doc.reference.update("active", true)
                                Toast.makeText(this, "Welcome ${doc.getString("name")}", Toast.LENGTH_SHORT).show()
                                setUser(doc)
                            }else{
//                    Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show()
                                if(binding.edtPassword.text.length < 6)
                                    Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_LONG).show()
                                Log.e(TAG,"Unable to create user", task.exception)
                            }
                        }
                }
            }


    }

    private fun setUser(usr:DocumentSnapshot){
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")

        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("user", User(usr.id, name, course, sec, grp, yer))
        }

        startActivity(intent)

    }

    private fun initializeValues(){
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = Firebase.auth
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
    }
}