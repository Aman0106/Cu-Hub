package com.example.cuhubapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivitySignUpBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firestore: FirebaseFirestore
    private val loadingDialog = LoadingDialog(this,"Setting things up...")

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            loadingDialog.startLoading()
            firestore.collection("users").whereEqualTo("firebaseUid",firebaseAuth.currentUser?.uid)
                .get().addOnSuccessListener {
                    loadingDialog.stopLoading()
                    setUser(it.documents[0])
                }
                .addOnFailureListener { 
                    loadingDialog.stopLoading()
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeValues()

        binding.btnSignIn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp(){
        loadingDialog.startLoading()
        val usr = firestore.collection("users")
            .whereEqualTo("email", binding.edtUserEmail.text.toString())

        usr.get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    Toast.makeText(this, "Your name is not in List", Toast.LENGTH_LONG).show()
                    loadingDialog.stopLoading()
                    return@addOnSuccessListener
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(binding.edtUserEmail.text.toString(),
                        binding.edtPassword.text.toString())
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                val doc = it.documents[0]
                                val firebaseUid = HashMap<String,String>()
                                firebaseUid["firebaseUid"] = firebaseAuth.currentUser!!.uid
                                doc.reference.update("active", true)
                                doc.reference.update("firebaseUid",firebaseAuth.currentUser!!.uid)
                                Toast.makeText(this, "Welcome ${doc.getString("name")}", Toast.LENGTH_SHORT).show()
                                setUser(doc)
                                loadingDialog.stopLoading()
                            }else{
                                loadingDialog.stopLoading()
                                try {
                                    throw task.exception!!
                                }catch (e:FirebaseAuthWeakPasswordException){
                                    Toast.makeText(this, "${e.reason}", Toast.LENGTH_SHORT).show()
                                    binding.edtPassword.requestFocus()
                                }catch (e:FirebaseAuthUserCollisionException){
                                    Toast.makeText(this, "User already Registered", Toast.LENGTH_LONG).show()
                                    binding.edtUserEmail.requestFocus()
                                }
                            }
                        }
                }
            }


    }

    private fun setUser(usr:DocumentSnapshot){
        val active = usr.getBoolean("active")
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")
        val firebaseUid = usr.getString("firebaseUid")

        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("user", User(active, usr.id, firebaseUid, name, course, sec, grp, yer))
        }

        startActivity(intent)

    }

    private fun initializeValues(){
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = Firebase.auth
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        styleSignInText()
    }

    private fun styleSignInText(){
        val txt = "Already a User? Sign In"
        val spannableString = SpannableString(txt)
        val clickableSpan:ClickableSpan = object : ClickableSpan(){
            override fun onClick(p0: View) {
                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.RED
            }
        }

        spannableString.setSpan(clickableSpan,16,spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtSignin.text = spannableString
        binding.txtSignin.movementMethod = LinkMovementMethod.getInstance()
    }
}