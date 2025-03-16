package com.filangladminlpuveggi.lpuveggii.Activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager

import com.filangladminlpuveggi.lpuveggii.adapter.searchadapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivitySearchBinding

import com.filangladminlpuveggi.lpuveggii.modelClass.products

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var b : ActivitySearchBinding
    lateinit var firestore :  FirebaseFirestore
    private lateinit var list : List<products?>
    lateinit var datalist: ArrayList<products>
     var searchRvadapter  = searchadapter(ArrayList() , this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(b.root)
        firestore = Firebase.firestore

        firestore.collection("Products").get()
            .addOnSuccessListener {result ->
                list  = result.toObjects(products::class.java)
                setRv()

            }
            .addOnFailureListener {
                Toast.makeText(this@SearchActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                setRv()
            }

        b.search.requestFocus()



    b.back.setOnClickListener {
       finish()
    }

    b.search.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
           if(6 == 5){
               Toast.makeText(this@SearchActivity, "", Toast.LENGTH_SHORT).show()
           }

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0.toString()!= ""){
                filter(p0.toString())
            }else {
                setRv()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            if(6 == 5){
                Toast.makeText(this@SearchActivity, "", Toast.LENGTH_SHORT).show()
            }
        }

    })


    }

    private fun filter(name: String) {
        var filterData = ArrayList<products>()
        for(i in list.indices){
            if(list[i]!!.name?.toLowerCase()!!.contains(name.lowercase())){
                filterData.add(list[i]!!)
            }

        }
        searchRvadapter.filterlist(filterData)


    }

    fun setRv(){
        datalist = ArrayList()
        b.rvSearch.layoutManager = LinearLayoutManager(this)

        for(i in list.indices){
            if(list[i]?.category!!.contains("Vegetables")){
                datalist.add(list[i]!!)
            }
        }
        searchRvadapter =  searchadapter(datalist , this)
        b.rvSearch.adapter = searchRvadapter
    }
}