package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.Room
import com.bumptech.glide.Glide
import com.filangladminlpuveggi.lpuveggii.Activities.cartActivity
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originalDataclass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originaldataEntityClas
import com.filangladminlpuveggi.lpuveggii.databinding.PopularproductsBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import java.util.logging.Handler

class BestProductsAdapter(var c  : Context) : Adapter<BestProductsAdapter.specialClassViewholder>() {
    private val room by lazy { Room.databaseBuilder(c , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    lateinit var cartdata : cartEntityClass
    private val originalDataRoom by lazy { Room.databaseBuilder(c , originalDataclass::class.java , "originaldataEntityClas")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    lateinit var originalDataEntityClass : originaldataEntityClas


    inner class specialClassViewholder(v : View) : ViewHolder(v){
        val b =  PopularproductsBinding.bind(v)
    }


    private val diffCallback = object : DiffUtil.ItemCallback<products>(){
        override fun areItemsTheSame(oldItem: products, newItem: products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: products, newItem: products): Boolean {
          return oldItem == newItem
        }

    }
   val differ =  AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): specialClassViewholder {
        return specialClassViewholder(
            LayoutInflater.from(c).inflate(R.layout.popularproducts , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: specialClassViewholder, position: Int) {
        val product = differ.currentList[position]

        val db = room.getDao()
        var list = db.returnname()

        holder.b.gram.text = product.sizes?.get(0)

        for (i in list) {
            if (i == product.name) {
                holder.b.btnAddtoCart.visibility = View.GONE
                holder.b.buttonLayout.visibility = View.VISIBLE
                var unit = db.getQuantity(i)
                var quantity = db.getSizeQuantity(i)
                holder.b.gram.text = quantity
                holder.b.sizetv.text = unit
            }
        }

        Glide.with(c).load(product.images[0]).into(holder.b.popularImage)
        holder.b.price.text = product.price.trim()

        var dprice = ((product.price.toInt() *10)/9).toString()
        holder.b.oPrice.text = dprice.toString()

        holder.b.name.text = product.name

        holder.itemView.setOnClickListener {
            onclick?.invoke(product)
        }


        holder.b.btnAddtoCart.setOnClickListener {
            holder.b.btnAddtoCart.visibility = View.GONE
            holder.b.progressBar.visibility = View.VISIBLE
            holder.b.blurBg.visibility = View.VISIBLE

            val db = room.getDao()
            val db2 = originalDataRoom.getDao()

            originalDataEntityClass = originaldataEntityClas(
                name = product.name,
                price = product.price,
                quantity = product.sizes?.get(0)!!.toString(),
                image = product.images[0],
                unit = "1"
            )
            cartdata = cartEntityClass(
                name = product.name,
                price = product.price,
                quantity = product.sizes?.get(0)!!.toString(),
                image = product.images[0],
                unit = "1"
            )

            db.insert(cartdata)
            db2.insertInOriginalData(originalDataEntityClass)
            holder.b.sizetv.text = "1"

            android.os.Handler(Looper.getMainLooper()).postDelayed({
                holder.b.blurBg.visibility = View.GONE
                holder.b.progressBar.visibility = View.GONE
                holder.b.buttonLayout.visibility = View.VISIBLE

            }, 1000)


            onclick2?.invoke(position)
        }


        holder.b.add.setOnClickListener {
            holder.b.sizetv.visibility = View.GONE
            holder.b.add.visibility = View.GONE
            holder.b.minus.visibility = View.GONE
            holder.b.sizetvprogressbar.visibility = View.VISIBLE

            var count = holder.b.sizetv.text.toString().toInt()
            count++
            holder.b.sizetv.text = count.toString()

            val db = room.getDao()
            var db2 = originalDataRoom.getDao()
            var list3 = db.getdata()
            var list2 = db2.getdatafromOrininalData()
            var sizeOfOriginPrice = ""
            var sizeOfPreviouPrice = ""

            for(i in list2){
                if(i.name == product.name){
                    sizeOfOriginPrice = i.price
                }
            }
            for(i in list3){
                if(i.name == product.name){
                    sizeOfPreviouPrice = i.price
                }
            }

//            var (price, pricetag) = separateValueAndUnit(sizeOfPreviouPrice) ?: error("Invalid input: ${sizeOfPreviouPrice}")
//            var (price2, pricetag2) = separateValueAndUnit(sizeOfOriginPrice) ?: error("Invalid input: $sizeOfOriginPrice")

            var x =  sizeOfPreviouPrice.toInt()
            x += sizeOfOriginPrice.toInt()

            db.update(product.name , product.sizes?.get(0).toString() , "$x", count.toString())

            android.os.Handler(Looper.getMainLooper()).postDelayed({
                holder.b.sizetv.visibility = View.VISIBLE
                holder.b.sizetvprogressbar.visibility = View.GONE
                holder.b.add.visibility = View.VISIBLE
                holder.b.minus.visibility = View.VISIBLE
            },1500)

        }


        holder.b.minus.setOnClickListener {
            var count = holder.b.sizetv.text.toString().toInt()
            count--
            holder.b.sizetv.text = count.toString()
            if (count == 0) {
                var db2 = originalDataRoom.getDao()
                db.deleteItem(product.name)
                db2.deleteItemfromOriginalData(product.name)
                holder.b.btnAddtoCart.visibility = View.VISIBLE
                onclick2?.invoke(position)
            }
            else {
                holder.b.sizetv.visibility = View.GONE
                holder.b.sizetvprogressbar.visibility = View.VISIBLE
                holder.b.add.visibility = View.GONE
                holder.b.minus.visibility = View.GONE

                val db = room.getDao()
                var db2 = originalDataRoom.getDao()
                var list3 = db.getdata()
                var list2 = db2.getdatafromOrininalData()
                var sizeOfOriginPrice = ""
                var sizeOfPreviouPrice = ""
                for(i in list2){
                    if(i.name == product.name){
                        sizeOfOriginPrice = i.price
                    }
                }
                for(i in list3){
                    if(i.name == product.name){
                        sizeOfPreviouPrice = i.price
                    }
                }


//
//
//                var (price, pricetag) = separateValueAndUnit(sizeOfPreviouPrice) ?: error("Invalid input: ${sizeOfPreviouPrice}")
//                var (price2, pricetag2) = separateValueAndUnit(sizeOfOriginPrice) ?: error("Invalid input: $sizeOfOriginPrice")
//
                  var x =  sizeOfPreviouPrice.toInt()
                   x  -= sizeOfOriginPrice.toInt()


                db.update(product.name , product.sizes?.get(0).toString() , "$x ", count.toString())
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    holder.b.sizetv.visibility = View.VISIBLE
                    holder.b.sizetvprogressbar.visibility = View.GONE
                    holder.b.add.visibility = View.VISIBLE
                    holder.b.minus.visibility = View.VISIBLE

                },1500)

            }

        }




        }
    
//      fun separateValueAndUnit(input: String): Pair<Int, String>? {
//        val regex = """\s*(\d+)\s*(\p{Alpha}+)""".toRegex() // Improved regex to handle spaces and non-ASCII units
//        val matchResult = regex.find(input)
//
//        return matchResult?.let {
//            val (value, unit) = it.destructured
//            Pair(value.toInt(), unit.trim())
//        }
//    }

    var onclick:((products) -> Unit)? = null
    var onclick2:((Int) -> Unit)? = null


}

