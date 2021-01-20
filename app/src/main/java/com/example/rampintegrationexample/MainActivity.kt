package com.example.rampintegrationexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * TODO() Fill parameters below with your values
     */
    private val rampHost = "buy.ramp.network"
    private val hostApiKey = "YOUR VALUE"
    private val userAddress = "YOUR VALUE"
    private val hostAppName = "YOUR VALUE"
    private val hostLogoUrl = "YOUR VALUE"
    private val finalUrl = "ramp-example://ramp.purchase.complete"
    private val swapAsset = "YOUR VALUE"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buyButton.setOnClickListener {
            showBrowser(composeUrl())
        }
        intent?.let { processIntent(it) }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun composeUrl(): String {
        return Uri.Builder()
            .scheme("https")
            .authority(rampHost)
            .appendQueryParameter("swapAsset", swapAsset)
            .appendQueryParameter("userAddress", userAddress)
            .appendQueryParameter("hostApiKey", hostApiKey)
            .appendQueryParameter("hostAppName", hostAppName)
            .appendQueryParameter("hostLogoUrl", hostLogoUrl)
            .appendQueryParameter("finalUrl", finalUrl)
            .build()
            .toString()
    }

    private fun showBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(link) }
        startActivity(intent)
    }

    private fun processIntent(intent: Intent) {
        val uri = intent.data?.toString()
        if (uri == finalUrl)
            Toast.makeText(this, "KUDOS! Purchase complete.", Toast.LENGTH_LONG).show()
    }
}
