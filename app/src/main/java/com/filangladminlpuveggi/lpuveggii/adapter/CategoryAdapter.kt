package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.CategoryItemLayoutBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.categoryDataClass

class categoryAdapter(var c : Context, var list : ArrayList<categoryDataClass>) : Adapter<categoryAdapter.viewHolder>() {
    inner class viewHolder(v : View) : ViewHolder(v){
        var b  = CategoryItemLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(c).inflate(R.layout.category_item_layout, parent , false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       var item = list[position]
        holder.b.name.text = item.name
        holder.b.image.setImageResource(item.image)
    }
}