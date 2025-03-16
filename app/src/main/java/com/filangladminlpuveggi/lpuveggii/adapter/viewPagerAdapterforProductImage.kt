package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ProductImageLayoutBinding
import org.jetbrains.annotations.Async

class viewPagerAdapterforProductImage(var c : Context) : Adapter<viewPagerAdapterforProductImage.viewHolder>() {

    inner class viewHolder(v: View) : ViewHolder(v){
      var b = ProductImageLayoutBinding.bind(v)
    }

    val differ =object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differItem = AsyncListDiffer(this , differ)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(c).inflate(R.layout.product_image_layout , parent , false))
    }

    override fun getItemCount(): Int {
        return differItem.currentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val i = differItem.currentList[position]
        Glide.with(c).load(i).into(holder.b.image)
    }


}