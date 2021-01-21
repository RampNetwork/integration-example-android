package com.example.rampintegrationexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * Define parameters according to your needs.
     * See [documentation](https://docs.ramp.network/configuration/)
     */
    private val userAddress = "1F1tAaz5x1HUXrsNLbtMDqcw6o5GNn4xqX"  // wallet address
    private val swapAsset = "BTC" // parameter that sets available crypto assets
    private val fiatCurrency = "EUR" // fiat  currency
    private val fiatValue = "1000" // total fiat value of the purchase that will be suggested to the user
    private val finalUrl = "ramp-example://ramp.purchase.complete"


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
            .authority("buy.ramp.network")
            .appendQueryParameter("swapAsset", swapAsset)
            .appendQueryParameter("userAddress", userAddress)
            .appendQueryParameter("fiatCurrency", fiatCurrency)
            .appendQueryParameter("fiatValue", fiatValue)
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
