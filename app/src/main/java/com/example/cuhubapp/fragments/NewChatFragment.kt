package com.example.cuhubapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentNewChatBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentNewChatBinding
//    private val loadingDialog: LoadingDialog? = activity?.let { LoadingDialog(it,"Fetching List") }

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UsersNewChatAdapter

    private lateinit var studentsList: ArrayList<User>
    private lateinit var curUser:User

    private lateinit var firestoreDatabase:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        initializeValues()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStudents(view)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getStudents(view: View){
        studentsList = ArrayList()
        firestoreDatabase.collection("users")
            .whereEqualTo("course",curUser.course)
            .whereEqualTo("year", curUser.yer)
            .whereEqualTo("section", curUser.section)
            .whereEqualTo("active",true)
            .get().addOnSuccessListener {
                for (student in it){
                    val stu = setUser(student)
                    if(stu.uid != curUser.uid)
                        studentsList.add(stu)
//                    loadingDialog?.stopLoading()
                }
                setRecyclerView(view)
            }.addOnFailureListener {
                Toast.makeText(view.context, "Unknown Error", Toast.LENGTH_SHORT).show()
//                loadingDialog?.stopLoading()
            }

    }

    private fun setUser(stu: QueryDocumentSnapshot): User{

        val active = stu.getBoolean("active")
        val uid = stu.id
        val name = stu.getString("name")
        val course = stu.getString("course")
        val sec = stu.getLong("section")
        val grp = stu.getString("group")
        val yer = stu.getLong("year")
        val firebaseUid = stu.getString("firebaseUid")
        return User(active, uid, firebaseUid, name, course, sec, grp, yer)
    }

    private fun setRecyclerView(view: View){
        userAdapter = UsersNewChatAdapter(view.context,studentsList)
        recyclerView = binding.recyclerview
        recyclerView.adapter = userAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun initializeValues(){
        val bundle = arguments
        firestoreDatabase = FirebaseFirestore.getInstance()
        curUser = bundle?.getParcelable("user")!!
//        loadingDialog?.startLoading()
    }
}