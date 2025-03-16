package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.Activities.AllOrders
import com.filangladminlpuveggi.lpuveggii.Orders.Order
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.AllOrdersCategoryBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.play.integrity.internal.c
import kotlinx.coroutines.GlobalScope

class allOrdersAdapter(var c : Context) : Adapter<allOrdersAdapter.viewHolder>() {
    inner class viewHolder(v : View) : ViewHolder(v){
        var b  = AllOrdersCategoryBinding.bind(v)
    }


    private val diffCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
    val differ =  AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
      return viewHolder(LayoutInflater.from(c).inflate(R.layout.all_orders_category , parent , false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       var item =  differ.currentList[position]

        if(holder.b.orderstatus.text.equals( "Ordered") ){
            holder.b.orderStatusColor1?.setBackgroundColor(c.resources.getColor(R.color.red))

        }else if(holder.b.orderstatus.text=="Canceled"){
            holder.b.orderStatusColor1?.setBackgroundColor(c.resources.getColor(R.color.red))


        }else if(holder.b.orderstatus.text == "Confirmed"){
            holder.b.orderStatusColor1?.setBackgroundColor(c.getColor(R.color.colorComplimentary))

        }else if(holder.b.orderstatus.text.equals("Shipped")){
            holder.b.orderStatusColor1?.setBackgroundColor(c.getColor(R.color.grey))

        }else if(holder.b.orderstatus.text == "Delivered"){
            holder.b.orderStatusColor1?.setBackgroundColor(c.getColor(R.color.colorPrimary))
        }

        holder.b.name.text =  "Order Id:- ${item.orderId.toString()}"
        holder.b.name2.text = item.date.toString()

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }

        holder.b.orderstatus.text = item.orderStatus.toString()


        Glide.with(c).load(item.products[0].image).into(holder.b.image)
    }

    var onClick:((Order) -> Unit)? = null
}