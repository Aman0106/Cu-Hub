package com.example.cuhubapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.MainActivity
import com.example.cuhubapp.activities.SignUpActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentLoginFacultyBinding
import com.example.cuhubapp.databinding.FragmentLoginStudentBinding
import com.example.cuhubapp.utils.StringParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginFacultyFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentLoginFacultyBinding

//    private val loadingDialog = LoadingDialog(requireActivity().parent,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginFacultyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeValues()
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
//        loadingDialog.startLoading()
        val email = StringParser().toLowerCase(binding.edtUserEmail.text.toString())
        firebaseAuth.signInWithEmailAndPassword(
            email,
            binding.edtUserPassword.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = firestore.collection("faculty")
                        .whereEqualTo("email", binding.edtUserEmail.text.toString())
                    doc.get()
                        .addOnSuccessListener {
                            setUser(it.documents[0])
                        }
                } else {
//                    loadingDialog.stopLoading()
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(activity, "Invalid Password", Toast.LENGTH_LONG).show()
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(activity, "User has not been Registered", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    private fun setUser(usr: DocumentSnapshot){
        val active = usr.getBoolean("active")
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")
        val firebaseUid = usr.getString("firebaseUid")

        val intent = Intent(activity, MainActivity::class.java).apply {
//            putExtra("user", User(active, usr.id, firebaseUid, name, course, sec, grp, yer))
        }

//        loadingDialog.stopLoading()
        startActivity(intent)
        requireActivity().finish()
    }

    private fun styleSignUpText(){
        val txt = "Not a User? Register"
        val spannable = SpannableString(txt)
        val clickableSignUpTxt: ClickableSpan = object : ClickableSpan(){
            override fun onClick(p0: View) {
                startActivity(Intent(activity, SignUpActivity::class.java))
                activity!!.finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.RED
            }
        }

        spannable.setSpan(clickableSignUpTxt,12, txt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtSignup.text = spannable
        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initializeValues(){
        firestore = FirebaseFirestore.getInstance()
        styleSignUpText()
        firebaseAuth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
    }

}