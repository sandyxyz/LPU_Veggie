package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.util.splitToIntList
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.adapter.cartAdapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityCartBinding
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection

class cartActivity : AppCompatActivity() {
    lateinit var b : ActivityCartBinding
    private val room by lazy { Room.databaseBuilder(this , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}

     lateinit var cartAdapter : cartAdapter
     var p : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCartBinding.inflate(layoutInflater)
        setContentView(b.root)

        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                b.networklayout.visibility = View.GONE

            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
                b.networklayout.visibility = View.VISIBLE
            }
        }

        b.cartRv.layoutManager = LinearLayoutManager(this)
        var db =  room.getDao()
        var list = db.getdata() as ArrayList<cartEntityClass>
        cartAdapter = cartAdapter(this ,list )
        b.cartRv.adapter = cartAdapter


        if(list.size > 0){
            b.cartRv.visibility = View.VISIBLE
            b.pricelayout.visibility = View.VISIBLE
            b.emptycart.visibility = View.GONE

            returnprice(b.price , b.offPercent)
        }


        cartAdapter.onclick = {
            if(list.size == 0){
                b.emptycart.visibility = View.VISIBLE
                b.pricelayout.visibility = View.GONE
            }
            returnprice(b.price , b.offPercent)
        }

        cartAdapter.onclick2 ={
            returnprice(b.price , b.offPercent)
        }



        b.back.setOnClickListener {
           finish()
        }
        b.btn.setOnClickListener{
            startActivity(Intent(this@cartActivity , MainHome::class.java))
            finish()
        }
        b.btnAddtoCart.setOnClickListener{

            var intent =   Intent(this@cartActivity , OrderSummary::class.java)
            intent.putExtra("price" , p.toString())
            startActivity(intent)
        }



    }
    private fun returnprice(t : TextView? , t2 : TextView){
        var db = room.getDao()
        var list = db.returnprice() as ArrayList<String>
        var tprice  = 0;

        for(i in list){
           var price  = i.toInt()
            tprice += price
        }

        t?.text = "₹"+ tprice.toString()
        var savedAmount = (tprice*10)/9  - tprice
        p = tprice
        t2.text = "Saved ₹${savedAmount} on this order"

    }


    private fun separateValueAndUnit(input: String): Pair<Int, String>? {
        val regex = """\s*(\d+)\s*(\p{Alpha}+)""".toRegex() // Improved regex to handle spaces and non-ASCII units
        val matchResult = regex.find(input)

        return matchResult?.let {
            val (value, unit) = it.destructured
            Pair(value.toInt(), unit.trim())
        }
    }
}