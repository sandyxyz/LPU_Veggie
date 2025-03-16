package com.filangladminlpuveggi.lpuveggii.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.adapter.ordersummaryAdapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityAllOrderDetailsBinding
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityAllOrdersBinding

class AllOrderDetails : AppCompatActivity() {
    lateinit var b : ActivityAllOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAllOrderDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)

        val args = AllOrderDetailsArgs.fromBundle(intent.extras ?: Bundle())
        val product = args.order


        b.basketRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false)
        var adapter = ordersummaryAdapter(this , product.products as ArrayList<cartEntityClass>)
        b.basketRv.adapter = adapter

        b.orderiD.text = "Order id:- ${product.orderId.toString()}"
        b.basketTv.text = "Basket(${product.products.size} items)"
        b.payableAmouttv.text = "Total Amount =  ${product.totalPrice}"

        b.back.setOnClickListener {
            finish()
        }

    }
}