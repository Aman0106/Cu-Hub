package com.example.cuhubapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.UserAdapter
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentChatsBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var recyclerView:RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter:UsersNewChatAdapter

    private lateinit var binding: FragmentChatsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var dbUserCollection:CollectionReference

    private lateinit var curUser: User
    private lateinit var chatUsersList:  ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeValues()
        setUserCards(view)
    }

    private fun setUserCards(view: View){
        userList = ArrayList()
        dbUserCollection.document(curUser.uid!!).collection("chats").get().addOnSuccessListener {
            for(doc in it){
                val student = firestore.collection("users").document(doc.id)
                setUser(student)
            }
            setRecyclerView(view)
        }
    }

    private fun setRecyclerView(view: View){
        userAdapter = UsersNewChatAdapter(view.context,userList)
        recyclerView = binding.recyclerview
        recyclerView.adapter = userAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun setUser(stu: DocumentReference){

        var userDoc = User()

        stu.get().addOnSuccessListener {
            val active = it.getBoolean("active")
            val uid = stu.id
            val name = it.getString("name")
            val course = it.getString("course")
            val sec = it.getLong("section")
            val grp = it.getString("group")
            val yer = it.getLong("year")
            val firebaseUid = it.getString("firebaseUid")
            userDoc = User(active, uid, firebaseUid, name, course, sec, grp, yer)
            if(firebaseUid == curUser.firebaseUid)
                return@addOnSuccessListener
            userList.add(0,userDoc!!)
            userAdapter.notifyItemInserted(userList.size-1)
            checkIfEmpty()
        }

    }

    private fun initializeValues(){
        firestore = FirebaseFirestore.getInstance()
        dbUserCollection = firestore.collection("users")
        userList = ArrayList()
        val bundle = arguments
        curUser = bundle?.getParcelable("user")!!
        checkIfEmpty()
    }

    private fun checkIfEmpty(){
        if(userList.isEmpty())
            binding.txtChatsDescription.text = "Your chats will appear here"
        else
            binding.txtChatsDescription.text = ""
    }

    private fun generateDummyList(size:Int): ArrayList<User>{
        val list = ArrayList<User>()

        for(i in 0 until size){
            val user = User("$i")
            list+=user
        }
        return list
    }

}
