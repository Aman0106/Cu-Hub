package com.example.cuhubapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.ImageViewActivity
import com.example.cuhubapp.activities.StudentListActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentProfileBinding

    private lateinit var firebaseStorage: FirebaseStorage
//    private lateinit var dbRefrerence: DatabaseR

    private lateinit var curUser: User

//    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firebaseStorage = Firebase.storage

        val bundle = arguments
        curUser = bundle?.getParcelable("user")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardTimeTable.setOnClickListener {
            val timeTableName:String = curUser.yer.toString() + "_" + curUser.course + "_" + curUser.section.toString() + ".png"
            val intent = Intent(view.context, ImageViewActivity::class.java).apply {
                putExtra("image","/time_tables/$timeTableName")
            }
            startActivity(intent)
        }
        binding.cardSection.setOnClickListener {
            val intent = Intent(view.context, StudentListActivity::class.java).apply {
                putExtra("showAll", true)
                putExtra("curUser", curUser)
            }
            startActivity(intent)
        }
        binding.cardGroup.setOnClickListener {
            val intent = Intent(view.context, StudentListActivity::class.java).apply {
                putExtra("showAll", false)
                putExtra("curUser", curUser)
            }
            startActivity(intent)
        }

        //TODO Later Faculty list
        binding.cardFaculty.setOnClickListener {
            Toast.makeText(view.context, "No Faculty list", Toast.LENGTH_SHORT).show()
        }

        
        setProfile(view)
    }

    private fun setProfile(view: View){
        binding.txtName.text = curUser.name
        binding.txtUid.text = curUser.uid
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun uploadImg(){

    }
}