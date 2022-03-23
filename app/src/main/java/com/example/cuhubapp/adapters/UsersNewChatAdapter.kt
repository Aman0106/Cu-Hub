package com.example.cuhubapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.activities.ChatActivity
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.UserCardBinding

class UsersNewChatAdapter(private val context: Context, private val userList:ArrayList<User>):RecyclerView.Adapter<UsersNewChatAdapter.UsersViewHolder>(){

    private lateinit var binding: UserCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        binding = UserCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return UsersViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = userList[position]
        holder.txtName.text = curUser.name
        if(curUser.uid?.length!! > 8)
        holder.txtUid.text = curUser.uid
    }

    override fun getItemCount() = userList.size

    inner class UsersViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = binding.userCardName
        val txtUid = binding.userCardMessage

        init {
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ChatActivity::class.java).apply {
                    putExtra("name", txtName.text)
                }

                context.startActivity(intent)
            }
        }
    }
}