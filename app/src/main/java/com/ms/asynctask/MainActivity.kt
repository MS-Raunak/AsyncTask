package com.ms.asynctask

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import okhttp3.OkHttpClient

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            FetchData().execute()
        }

    }

    // Creating a Class

    internal inner class FetchData : AsyncTask<Void, Void, String>() {
        val tvResult = findViewById<TextView>(R.id.tvResult)
        var hasInternet = false

        private lateinit var progressDialog : ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Downloading...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): String? {
            if (isNetworkAvailable()) {
                hasInternet = true
                val client = OkHttpClient()
                val urlLink = "https://pokeapi.co/api/v2/pokemon/"
                val request = okhttp3.Request.Builder().url(urlLink).build()
                val response = client.newCall(request).execute()
                return response.body()?.string().toString()
            } else{
                return " "
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

                if (hasInternet)
                    tvResult.text = result
                else
                    tvResult.text = "No Internet"
        }

    }

    private fun isNetworkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activieNetworkInfo = connectivityManager.activeNetworkInfo
        return activieNetworkInfo != null && activieNetworkInfo.isConnected

    }
}