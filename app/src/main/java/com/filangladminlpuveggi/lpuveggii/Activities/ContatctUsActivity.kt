package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityContatctUsBinding

class ContatctUsActivity : AppCompatActivity() {
    lateinit var b : ActivityContatctUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityContatctUsBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.whatspp.setOnClickListener {
            val phoneNumber = "+918210632703"
            openWhatsAppChat(phoneNumber)
        }
        b.instagram.setOnClickListener {
            val userName = "filangcorp"
            openInstagramAccount(userName)
        }
        b.gmail.setOnClickListener {
            // Replace "recipient@example.com" with the email address you want to send the email to
            val emailAddress = "sandhyaanandam2121@gmail.com"
            composeEmail(emailAddress)
        }
        b.back.setOnClickListener {
            finish()
        }

    }
    private fun composeEmail(emailAddress: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us")
        startActivity(intent)


    }
    private fun openWhatsAppChat(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("https://wa.me/$phoneNumber")
        intent.data = uri
        startActivity(intent)
    }

    private fun openInstagramAccount(username: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("https://www.instagram.com/$username")
        intent.data = uri
        startActivity(intent)

    }
}