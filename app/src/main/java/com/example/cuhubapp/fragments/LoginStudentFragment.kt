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
import com.example.cuhubapp.activities.UserType
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityLogInBinding
import com.example.cuhubapp.databinding.FragmentLoginStudentBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.example.cuhubapp.utils.StringParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginStudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentLoginStudentBinding

    private lateinit var loadingDialog:LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadingDialog = LoadingDialog(activity as LogInActivity,"Loging in")
        binding = FragmentLoginStudentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeValues()
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        loadingDialog.startLoading()
        val email = StringParser().toLowerCase(binding.edtUserEmail.text.toString())
        firebaseAuth.signInWithEmailAndPassword(
            email,
            binding.edtUserPassword.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = firestore.collection("users")
                        .whereEqualTo("email", binding.edtUserEmail.text.toString())
                    doc.get()
                        .addOnSuccessListener {
                            if(it.isEmpty){
                                Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                                loadingDialog.stopLoading()
                                return@addOnSuccessListener
                            }
                            setUser(it.documents[0])
                        }
                } else {
                    loadingDialog.stopLoading()
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
        val path = usr.reference.path

        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("user", User(active, usr.id, firebaseUid, name, course, sec, grp, yer, path))
            putExtra("userType", UserType.STUDENT)
        }

        loadingDialog.stopLoading()
        startActivity(intent)
        activity?.finish()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginStudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}