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
import com.example.cuhubapp.activities.LogInActivity
import com.example.cuhubapp.activities.MainActivity
import com.example.cuhubapp.activities.SignUpActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivitySignUpBinding
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

class RegisterStudentFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding:FragmentRegisterStudentBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterStudentBinding.inflate(inflater,container,false)
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
        val usr = firestore.collection("users")
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
                                setUser(doc)
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

    private fun setUser(usr: DocumentSnapshot){
        val active = usr.getBoolean("active")
        val name = usr.getString("name")
        val course = usr.getString("course")
        val sec = usr.getLong("section")
        val grp = usr.getString("group")
        val yer = usr.getLong("year")
        val firebaseUid = usr.getString("firebaseUid")

        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("user", User(active, usr.id, firebaseUid, name, course, sec, grp, yer))
        }

        startActivity(intent)

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