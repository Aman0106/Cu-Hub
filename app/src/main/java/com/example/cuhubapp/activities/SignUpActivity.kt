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
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivitySignUpBinding
import com.example.cuhubapp.fragments.RegisterFacultyFragment
import com.example.cuhubapp.fragments.RegisterStudentFragment
import com.example.cuhubapp.utils.LoadingDialog
import com.example.cuhubapp.utils.StringParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
            firebaseAuth.signOut()
            loadingDialog.startLoading()
            firestore.collection("user").whereEqualTo("firebaseUid",firebaseAuth.currentUser?.uid)
                .get().addOnCompleteListener {
                    if(!it.result.isEmpty) {
                        loadingDialog.stopLoading()
                        setUser(it.result.documents[0])
                        loadingDialog.stopLoading()
                        return@addOnCompleteListener
                    }
                    firestore.collection("faculty").whereEqualTo("firebaseUid",firebaseAuth.currentUser?.uid)
                        .get().addOnSuccessListener {doc ->
                            loadingDialog.stopLoading()
                            setFacultyUser(doc.documents[0])
                        }
                        .addOnFailureListener {exc->
                            Toast.makeText(this, "${exc.message}", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        binding.navigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_student_mode->{
                    replaceFragment(RegisterStudentFragment())
                }
                R.id.item_faculty_mode->{
                    replaceFragment(RegisterFacultyFragment())
                }
            }

            return@setOnItemSelectedListener true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeValues()
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

    private fun setFacultyUser(usr:DocumentSnapshot){
        val active = usr.getBoolean("active")
        val name = usr.getString("name")
        val firebaseUid = usr.getString("firebaseUid")

        val intent = Intent(this,FacultyMainActivity::class.java).apply {
            putExtra("facultyuser", FacultyUser(active,usr.id, name, firebaseUid))
        }

        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun initializeValues(){
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = Firebase.auth
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()

        replaceFragment(RegisterStudentFragment())
    }

//    private fun styleSignInText(){
//        val txt = "Already a User? Sign In"
//        val spannableString = SpannableString(txt)
//        val clickableSpan:ClickableSpan = object : ClickableSpan(){
//            override fun onClick(p0: View) {
//                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
//                finish()
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.color = Color.RED
//            }
//        }
//
//        spannableString.setSpan(clickableSpan,16,spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.txtSignin.text = spannableString
//        binding.txtSignin.movementMethod = LinkMovementMethod.getInstance()
//    }
}
