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
import com.example.cuhubapp.activities.FacultyMainActivity
import com.example.cuhubapp.activities.LogInActivity
import com.example.cuhubapp.activities.MainActivity
import com.example.cuhubapp.activities.SignUpActivity
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentRegisterFacultyBinding
import com.example.cuhubapp.databinding.FragmentRegisterStudentBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.example.cuhubapp.utils.StringParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
class RegisterFacultyFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentRegisterFacultyBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var loadingDialog:LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterFacultyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeValues()
        loadingDialog = LoadingDialog(activity as SignUpActivity,"")
        binding.btnSignIn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        loadingDialog.startLoading()
        val email = StringParser().toLowerCase(binding.edtUserEmail.text.toString())
        val usr = firestore.collection("faculty")
            .whereEqualTo("email", email)

        usr.get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(activity, "Your name is not in List", Toast.LENGTH_LONG).show()
                    loadingDialog.stopLoading()
                    return@addOnSuccessListener
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(
                        email,
                        binding.edtUserPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val doc = it.documents[0]
                                val firebaseUid = HashMap<String, String>()
                                firebaseUid["firebaseUid"] = firebaseAuth.currentUser!!.uid
                                doc.reference.update("active", true)
                                doc.reference.update("firebaseUid", firebaseAuth.currentUser!!.uid)
                                Toast.makeText(
                                    activity,
                                    "Welcome ${doc.getString("name")}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setFacultyUser(doc)
                                loadingDialog.stopLoading()
                            } else {
                                loadingDialog.stopLoading()
                                try {
                                    throw task.exception!!
                                } catch (e: FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(activity, "${e.reason}", Toast.LENGTH_SHORT)
                                        .show()
                                    binding.edtUserPassword.requestFocus()
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    Toast.makeText(
                                        activity,
                                        "User already Registered",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.edtUserEmail.requestFocus()
                                }
                            }
                        }
                }
            }
    }

    private fun setFacultyUser(usr:DocumentSnapshot){
        val active = usr.getBoolean("active")
        val name = usr.getString("name")
        val firebaseUid = usr.getString("firebaseUid")

        val intent = Intent(activity, FacultyMainActivity::class.java).apply {
            putExtra("facultyuser", FacultyUser(active,usr.id, name, firebaseUid))
        }

        startActivity(intent)
        activity?.finish()
    }

    private fun styleSignInText(){
        val txt = "Already a User? Sign In"
        val spannableString = SpannableString(txt)
        val clickableSpan: ClickableSpan = object : ClickableSpan(){
            override fun onClick(p0: View) {
                startActivity(Intent(activity, LogInActivity::class.java))
                activity?.finish()
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

    private fun initializeValues(){
        firebaseAuth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        styleSignInText()
    }
}