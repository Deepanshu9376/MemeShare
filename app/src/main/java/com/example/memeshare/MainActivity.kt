@file:Suppress("RedundantSamConstructor")

package com.example.memeshare

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val urlNew=response.getString("url")
                val imageView = findViewById<ImageView>(R.id.ImageView)

                Glide.with(this).load(urlNew).into(imageView)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Something Went Wrong error",Toast.LENGTH_LONG).show()
            }
        )

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }
    fun shareMeme(view: View) {}
    fun nextMeme(view: View) {}
}