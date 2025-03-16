package com.filangladminlpuveggi.lpuveggii.adapter

import android.content.Context
import android.os.Looper
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
import com.filangladminlpuveggi.lpuveggii.databinding.CartLayoutBinding
import com.google.android.play.integrity.internal.i

class cartAdapter(var c : Context, var list : ArrayList<cartEntityClass>?) : Adapter<cartAdapter.viewhol>() {

    private val room by lazy { Room.databaseBuilder(c , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}



    private val originalDataRoom by lazy { Room.databaseBuilder(c , originalDataclass::class.java , "originaldataEntityClas")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}



    inner class viewhol(v : View) : ViewHolder(v){
        var b = CartLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewhol {
        return viewhol(LayoutInflater.from(c).inflate(R.layout.cart_layout , parent , false))
    }

    override fun getItemCount(): Int {
      return list!!.size
    }

    override fun onBindViewHolder(holder: viewhol, position: Int) {
        val item = list?.get(position)
        var db22 = originalDataRoom.getDao()

        var list22 = db22.getdatafromOrininalData()
        var sizeOfOriginProduct1 = list22[position].quantity
        var sizeOfOriginPrice1 = list22[position].price

        holder.b.name.text = item?.name.toString()
        holder.b.price.text = item?.price.toString()
        holder.b.size.text = item?.quantity.toString()
        holder.b.price2.text = sizeOfOriginPrice1
        holder.b.size2.text = sizeOfOriginProduct1
        holder.b.sizetv.text = item?.unit.toString()

        Glide.with(c).load(item?.image).into(holder.b.image)


        holder.b.delete.setOnClickListener {
            var db =  room.getDao()
            var db2 = originalDataRoom.getDao()


            if (item != null) {
                db.deleteItem(item.name)
                db2.deleteItemfromOriginalData(item.name)
            }
            list?.removeAt(position)
            notifyDataSetChanged()

            onclick?.invoke(position)
        }

        var db2 = originalDataRoom.getDao()

        var list2 = db2.getdatafromOrininalData()
       var sizeOfOriginProduct = list2[position].quantity
       var sizeOfOriginPrice = list2[position].price

        var (size, unit1) = separateValueAndUnit(holder.b.size.text.toString()) ?: error("Invalid input: ${holder.b.size.text.toString()}")
        var (size2, unit2) = separateValueAndUnit(sizeOfOriginProduct) ?: error("Invalid input: $sizeOfOriginProduct")



        holder.b.add.setOnClickListener {
            holder.b.sizetv.visibility = View.GONE
            holder.b.add.visibility = View.GONE
            holder.b.minus.visibility = View.GONE
            holder.b.sizetvprogressbar.visibility = View.VISIBLE


            var db = room.getDao()
            var count = holder.b.sizetv.text.toString().toInt()
            ++count

            size += size2
            var x  = (holder.b.price.text as String).toInt()
            x += sizeOfOriginPrice.toInt()



            holder.b.size.text = size.toString() +" "+ unit1
            holder.b.price.text =  x.toString()

            holder.b.sizetv.text = count.toString()
            db.update(item!!.name , size2.toString()+" "+ unit2, holder.b.price.text.toString() , holder.b.sizetv.text.toString())


            android.os.Handler(Looper.getMainLooper()).postDelayed({
                holder.b.sizetv.visibility = View.VISIBLE
                holder.b.sizetvprogressbar.visibility = View.GONE
                holder.b.add.visibility = View.VISIBLE
                holder.b.minus.visibility = View.VISIBLE
            },1000)

            onclick2?.invoke(position)

        }
        holder.b.minus.setOnClickListener {
            holder.b.sizetv.visibility = View.GONE
            holder.b.add.visibility = View.GONE
            holder.b.minus.visibility = View.GONE
            holder.b.sizetvprogressbar.visibility = View.VISIBLE

            var db = room.getDao()
            var count = holder.b.sizetv.text.toString().toInt()

            if(count > 1){
                count--
                size -= size2

                var x  = (holder.b.price.text as String).toInt()
                x -= sizeOfOriginPrice.toInt()

                holder.b.size.text = size.toString() +" "+ unit1
                holder.b.price.text =  x.toString()


                holder.b.sizetv.text = count.toString()

                db.update(item!!.name ,  size2.toString()+" "+ unit2 , holder.b.price.text.toString() , holder.b.sizetv.text.toString())

                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    holder.b.sizetv.visibility = View.VISIBLE
                    holder.b.sizetvprogressbar.visibility = View.GONE
                    holder.b.add.visibility = View.VISIBLE
                    holder.b.minus.visibility = View.VISIBLE
                },1000)

                onclick2?.invoke(position)
            }
        }

    }


    fun separateValueAndUnit(input: String): Pair<Int, String>? {
        val regex = """\s*(\d+)\s*(\p{Alpha}+)""".toRegex() // Improved regex to handle spaces and non-ASCII units
        val matchResult = regex.find(input)

        return matchResult?.let {
            val (value, unit) = it.destructured
            Pair(value.toInt(), unit.trim())
        }
    }

    var onclick : ((Int) -> Unit)? = null
    var onclick2 : ((Int) -> Unit)? = null

}