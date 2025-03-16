package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.filangladminlpuveggi.lpuveggii.ViewModel.MainPageViewModel
import com.filangladminlpuveggi.lpuveggii.ViewModel.OrderViewModel
import com.filangladminlpuveggi.lpuveggii.adapter.allOrdersAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.scrollerAdapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityAllOrdersBinding
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrders : AppCompatActivity() {
    lateinit var b : ActivityAllOrdersBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private val viewmodel by viewModels<OrderViewModel>()
    private val allOrdersAdapter by lazy { allOrdersAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAllOrdersBinding.inflate(layoutInflater)
        setContentView(b.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        b.back.setOnClickListener {
            finish()
        }

        allOrdersAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable("order", it)
            }
            val intent = Intent(this, AllOrderDetails::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

        }

        var lm = LinearLayoutManager(this)
        lm.reverseLayout = true
        lm.stackFromEnd = true
        b.allOrderRV.layoutManager = lm


        lifecycleScope.launchWhenStarted {
            viewmodel.allOrder.collectLatest {
                when (it) {
                    is Resourse.Loading -> {
                        b.progresBar.visibility =  View.VISIBLE
                    }

                    is Resourse.Success -> {
                       allOrdersAdapter.differ.submitList(it.data)
                        b.progresBar.visibility =  View.GONE
                        b.allOrderRV.adapter = allOrdersAdapter
                        b.allOrderRV.smoothScrollToPosition(0)

                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@AllOrders, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }




//        var db = allordersroom.getAll()
//
//        b.allOrderRV.layoutManager =  LinearLayoutManager(this)
//        var list = db.getAllOrdersData()
//        var adapter =  allOrdersAdapter(this , list)
//        b.allOrderRV.adapter = adapter

    }
}