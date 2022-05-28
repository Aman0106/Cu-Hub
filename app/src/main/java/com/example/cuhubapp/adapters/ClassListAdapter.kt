package com.example.cuhubapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.activities.ChatActivity
import com.example.cuhubapp.activities.StudentListActivity
import com.example.cuhubapp.classes.Classes
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ClassCardBinding
import com.example.cuhubapp.databinding.UserCardBinding
import com.example.cuhubapp.fragments.ListType
import com.example.cuhubapp.utils.StringParser

class ClassListAdapter(
    private val context:Context,
    private val classList:ArrayList<Classes>,
    private val sender:String?): RecyclerView.Adapter<ClassListAdapter.ClassesViewHolder>() {

    private lateinit var binding: ClassCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        binding = ClassCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClassesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        val curCard = classList[position]
        holder.txtName.text = curCard.name
        holder.info = curCard.rawName!!
    }

    override fun getItemCount() = classList.size

    inner class ClassesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = binding.classCardName
        var info = ""
        init {
            itemView.setOnClickListener{
                val l = StringParser().extractYearCourseSectionGroup(info)
                val intent = Intent(itemView.context, StudentListActivity::class.java).apply {
                    val usr = User(l[1], l[2].toLongOrNull(), l[3], l[0].toLongOrNull(), sender)
                    putExtra("curUser", usr)
                    putExtra("listType", ListType.SECTION)
                }
                context.startActivity(intent)
            }
        }
    }
}