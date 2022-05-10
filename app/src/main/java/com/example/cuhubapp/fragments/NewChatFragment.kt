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

class NewChatFragment : Fragment() {

    private lateinit var binding: FragmentNewChatBinding
//    private val loadingDialog: LoadingDialog? = activity?.let { LoadingDialog(it,"Fetching List") }

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UsersNewChatAdapter

    private lateinit var studentsList: ArrayList<User>
    private lateinit var curUser:User

    private lateinit var firestoreDatabase:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val path = stu.reference.path

        return User(active, uid, firebaseUid, name, course, sec, grp, yer, path)
    }

    private fun setRecyclerView(view: View){
        userAdapter = UsersNewChatAdapter(view.context,studentsList, curUser.path)
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