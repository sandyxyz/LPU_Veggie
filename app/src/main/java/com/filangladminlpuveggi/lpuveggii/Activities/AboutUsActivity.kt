package com.filangladminlpuveggi.lpuveggii.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityAboutUsBinding
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityContatctUsBinding

class AboutUsActivity : AppCompatActivity() {
    lateinit var b : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.back.setOnClickListener {
            finish()
        }



    }
}