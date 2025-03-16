package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.Room
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originalDataclass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originaldataEntityClas
import com.filangladminlpuveggi.lpuveggii.databinding.BasketLayoutBinding
import com.filangladminlpuveggi.lpuveggii.databinding.CartLayoutBinding
import com.google.android.play.integrity.internal.i

class ordersummaryAdapter(var c : Context, var list : ArrayList<cartEntityClass>?) : Adapter<ordersummaryAdapter.viewhol>() {

    private val originalDataRoom by lazy { Room.databaseBuilder(c , originalDataclass::class.java , "originaldataEntityClas")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}



    inner class viewhol(v : View) : ViewHolder(v){
        var b = BasketLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewhol {
        return viewhol(LayoutInflater.from(c).inflate(R.layout.basket_layout , parent , false))
    }

    override fun getItemCount(): Int {
      return list!!.size
    }

    override fun onBindViewHolder(holder: viewhol, position: Int) {
        val item = list?.get(position)
        var db22 = originalDataRoom.getDao()



        holder.b.name.text = item?.name.toString()
       // var quantity =  db22.returnSize(item?.name!!)
        holder.b.size.text = item?.quantity


        holder.b.quantity.text =  "×"+item?.unit.toString()
        Glide.with(c).load(item?.image).into(holder.b.imageView)

    }

////
//    fun separateValueAndUnit(input: String): Pair<Int, String>? {
//        val regex = """\s*(\d+)\s*(\p{Alpha}+)""".toRegex() // Improved regex to handle spaces and non-ASCII units
//        val matchResult = regex.find(input)
//
//        return matchResult?.let {
//            val (value, unit) = it.destructured
//            Pair(value.toInt(), unit.trim())
//        }
//    }
//

}