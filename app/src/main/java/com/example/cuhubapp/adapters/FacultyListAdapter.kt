package com.example.cuhubapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.activities.ChatActivity
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.databinding.FacultyUserCardBinding
import com.example.cuhubapp.databinding.UserCardBinding
import com.google.firebase.firestore.DocumentReference

class FacultyListAdapter(private val context: Context,
                         private val facultyList:ArrayList<FacultyUser>,
                         private val sender:String?): RecyclerView.Adapter<FacultyListAdapter.UsersViewHolder>() {

    private lateinit var binding: FacultyUserCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        binding = FacultyUserCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return UsersViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = facultyList[position]
        holder.txtName.text = curUser.name
        holder.firebaseUid = curUser.firebaseUid
        holder.active = curUser.active
        holder.receiver = curUser.path
        if(curUser.uid?.length!! > 8)
            holder.txtUid.text = curUser.uid
    }

    override fun getItemCount() = facultyList.size

    inner class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = binding.userCardName
        val txtUid = binding.userCardMessage
        val txtSubject = binding.facultyCardSubject
        var firebaseUid:String? = null
        var active:Boolean? = null
        var receiver:String? = null

        init {
            itemView.setOnClickListener{
                if(active == null){
                    Toast.makeText(itemView.context, "User has not registered", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(itemView.context, ChatActivity::class.java).apply {
                    putExtra("name", txtName.text)
                    putExtra("firebaseUid", firebaseUid)
                    putExtra("sender",sender)
                    putExtra("receiver",receiver)
                }
                context.startActivity(intent)
            }
        }
    }
}