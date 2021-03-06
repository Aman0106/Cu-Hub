package com.example.cuhubapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.activities.ChatActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.UserCardBinding

class UsersNewChatAdapter(private val context: Context,
                          private val userList:ArrayList<User>,
                          private val sender:String?):RecyclerView.Adapter<UsersNewChatAdapter.UsersViewHolder>(){

    private lateinit var binding: UserCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        binding = UserCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return UsersViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = userList[position]
        holder.txtName.text = curUser.name
        holder.firebaseUid = curUser.firebaseUid
        holder.active = curUser.active
        holder.receiver = curUser.path
        if(curUser.uid?.length!! > 8)
            holder.txtUid.text = curUser.uid
    }

    override fun getItemCount() = userList.size

    inner class UsersViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = binding.userCardName
        val txtUid = binding.userCardMessage
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
                    putExtra("sender", sender)
                    putExtra("receiver", receiver)
                }
                context.startActivity(intent)
            }
        }
    }

}