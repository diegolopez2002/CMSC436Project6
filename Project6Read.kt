package com.example.project6

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Project6Read {

    private val client = OkHttpClient()

    fun readData(email: String, onResponse: (String, Int) -> Unit, onFailure: (String) -> Unit) {
        val url = "https://cmsc436-2301.cs.umd.edu/project6Read.php?email=$email"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Request failed: ${e.message}")
                onFailure("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("MainActivity", "READ response: $responseData")
                if (response.isSuccessful) {
                    try {
                        val jsonResponse = responseData?.let { JSONObject(it) }
                        val found = jsonResponse?.getString("found") ?: "no"
                        if (found == "yes") {
                            val data = jsonResponse?.getJSONArray("data")
                            val name = data?.getString(0)
                            val number = data?.getInt(1)
                            val result = "$name will be $number years old"
                            if (number != null) {
                                onResponse(result, number)
                            }
                        } else {
                            onResponse("NA", -1)
                        }
                    } catch (e: Exception) {
                        onFailure("JSON parsing error")
                    }
                } else {
                    onFailure("Server returned an error: ${response.message}")
                }
            }
        })
    }
}
