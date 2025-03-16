package com.filangladminlpuveggi.lpuveggii.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.Activities.ProductDetailsActivity
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.SearchItemRvBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products

class searchadapter(var datalist : ArrayList<products> , var c : Context) :  Adapter<searchadapter.viewHolder>() {
    inner class viewHolder(item : View) : ViewHolder(item){
        var b = SearchItemRvBinding.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(c).inflate(R.layout.search_item_rv , parent , false))
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterlist(filterlist :  ArrayList<products>){
        datalist = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val product = datalist[position]
        holder.b.title.text = product.name.toString()
        Glide.with(c).load(datalist[position].images[0].toString()).into(holder.b.itemimage)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            val intent = Intent(c, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK
            c.startActivity(intent)

        }
    }
}