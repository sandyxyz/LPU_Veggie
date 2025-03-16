package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.ViewModel.MainPageViewModel
import com.filangladminlpuveggi.lpuveggii.adapter.BestProductsAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.CategoryPageAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.SpecialProductsAdapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityCategoryBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    lateinit var b : ActivityCategoryBinding
    lateinit var categoryAdapter: CategoryPageAdapter
     var list  = ArrayList<products>()
    var firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b =  ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.categoryRV.apply {
            layoutManager =
                GridLayoutManager(this@CategoryActivity, 2)


        }

        b.back.setOnClickListener {
            finish()
        }

        var ss = intent.getStringExtra("na")
        categoryAdapter = CategoryPageAdapter(this , list )
        b.titleTv.text = ss.toString()

        firestore.collection("Products").whereEqualTo("category" , ss)
            .get()
            .addOnSuccessListener { result ->
                val specialProductslist = result.toObjects(products::class.java) as ArrayList<products>
                 list =  specialProductslist
                categoryAdapter = CategoryPageAdapter(this , list )
                b.categoryRV.adapter = categoryAdapter
                b.progresBar.visibility = View.GONE
                b.categoryRV.visibility = View.VISIBLE

            }
            .addOnFailureListener {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                b.progresBar.visibility = View.GONE

            }




//        categoryAdapter.onclick = {
//
//        }

    }

}