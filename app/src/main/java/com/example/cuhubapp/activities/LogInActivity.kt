package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cuhubapp.R
import com.example.cuhubapp.databinding.ActivityLogInBinding
import com.example.cuhubapp.fragments.LoginFacultyFragment
import com.example.cuhubapp.fragments.LoginStudentFragment
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLogInBinding

    private val loadingDialog = LoadingDialog(this,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()
        initializeValues()

        binding.navigationBar.setOnItemSelectedListener{
            when(it.itemId){
                R.id.item_faculty_mode->{
                    replaceFragment(LoginFacultyFragment())
                }
                R.id.item_student_mode->{
                    replaceFragment(LoginStudentFragment())
                }
            }

            return@setOnItemSelectedListener true
        }

    }



//    private fun setUser(usr: DocumentSnapshot){
//        val active = usr.getBoolean("active")
//        val name = usr.getString("name")
//        val course = usr.getString("course")
//        val sec = usr.getLong("section")
//        val grp = usr.getString("group")
//        val yer = usr.getLong("year")
//        val firebaseUid = usr.getString("firebaseUid")
//
//        val intent = Intent(this,MainActivity::class.java).apply {
//            putExtra("user", User(active, usr.id, firebaseUid, name, course, sec, grp, yer))
//        }
//
//        loadingDialog.stopLoading()
//        startActivity(intent)
//        finish()
//    }

//    private fun styleSignUpText(){
//        val txt = "Not a User? Register"
//        val spannable = SpannableString(txt)
//        val clickableSignUpTxt: ClickableSpan = object : ClickableSpan(){
//            override fun onClick(p0: View) {
//                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
//                finish()
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.color = Color.RED
//            }
//        }
//
//        spannable.setSpan(clickableSignUpTxt,12, txt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.txtSignup.text = spannable
//        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
//    }

    private fun initializeValues(){
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
//        styleSignUpText()
        firebaseAuth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        replaceFragment(LoginStudentFragment())
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

//        val bundle = Bundle()
//        bundle.putParcelable("user", curUser)
//        bundle.putParcelableArrayList("usersInSection",usersInSection)
//        fragment.arguments = bundle

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }

}