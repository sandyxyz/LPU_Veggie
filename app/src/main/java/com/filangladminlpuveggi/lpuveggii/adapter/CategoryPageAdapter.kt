package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.Room
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.Activities.ProductDetailsActivity
import com.filangladminlpuveggi.lpuveggii.Activities.cartActivity
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originalDataclass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originaldataEntityClas
import com.filangladminlpuveggi.lpuveggii.ViewModel.MainPageViewModel
import com.filangladminlpuveggi.lpuveggii.databinding.CategoryLayoutBinding
import com.filangladminlpuveggi.lpuveggii.databinding.MostlovedproductsBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products


class CategoryPageAdapter(var c  : Context  , var list : ArrayList<products>) : Adapter<CategoryPageAdapter.specialClassViewholder>() {


    inner class specialClassViewholder(v: View) : ViewHolder(v) {
        val b = CategoryLayoutBinding.bind(v)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): specialClassViewholder {
        return specialClassViewholder(
            LayoutInflater.from(c).inflate(R.layout.category_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: specialClassViewholder, position: Int) {
        val product = list[position]


        holder.b.name.text = product.name
        holder.b.price.text = product.price.trim()

        var dprice = ((product.price.toInt() *12)/9).toString()
        holder.b.oPrice.text = dprice.toString()

        Glide.with(c).load(product.images[0]).into(holder.b.popularImage)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            val intent = Intent(c, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            c.startActivity(intent)


        }

    }






}





