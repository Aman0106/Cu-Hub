package com.example.cuhubapp.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityLogInBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var edtUserId: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var binding: ActivityLogInBinding

    private val loadingDialog = LoadingDialog(this,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()
        initializeValues()

        binding.btnSignIn.setOnClickListener {
            loadingDialog.startLoading()
            firebaseAuth.signInWithEmailAndPassword(binding.edtUserEmail.text.toString(),
                binding.edtUserPassword.text.toString())
                .addOnCompleteListener{ task -> 
                    if(task.isSuccessful) {
                        val doc = firestore.collection("users")
                            .whereEqualTo("email", binding.edtUserEmail.text.toString())
                        doc.get()
                            .addOnSuccessListener {
                                setUser(it.documents[0])
                            }
                    }
                    else{
                        Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUser(usr: DocumentSnapshot){
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")

        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("user", User(usr.id, name, course, sec, grp, yer))
        }

        loadingDialog.stopLoading()
        startActivity(intent)

    }

    private fun signUpText(){
        var txt = "Not a User? Sign Up"
        var spannable = SpannableString(txt)
        // fcs = ForegroundColorSpanRed
        var fcsRed = ForegroundColorSpan(Color.RED)

        val clickableSignUpTxt: ClickableSpan = object : ClickableSpan(){
            override fun onClick(p0: View) {
                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.RED
            }
        }


//        spannable.setSpan(fcsRed,12, txt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(clickableSignUpTxt,12, txt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtSignup.text = spannable
        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initializeValues(){
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        signUpText()
        firebaseAuth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
    }

}