package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.SizeLayoutBinding

class sizeAdapter(var c : Context) : RecyclerView.Adapter<sizeAdapter.viewhold>() {
    var selectedposition = -1
    inner class viewhold(v : View) : ViewHolder(v){
      var b =  SizeLayoutBinding.bind(v)
    }

    val differitem = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this , differitem)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewhold {
       return viewhold(LayoutInflater.from(c).inflate(R.layout.size_layout , parent , false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewhold, position: Int) {
       var i  = differ.currentList[position]

        holder.b.size.text = i
        if(position == selectedposition){
            holder.b.backgroud.visibility = View.VISIBLE
        }else{
            holder.b.backgroud.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            if(selectedposition >= 0){
                notifyItemChanged(selectedposition)
            }
            selectedposition = holder.adapterPosition
            notifyItemChanged(selectedposition)
            onItemClick?.invoke(i)
        }
    }

    var onItemClick : ((String) -> Unit)? = null
}