package com.example.project6

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Project6Write {

    private val client = OkHttpClient()

    fun sendJsonRequest(email: String, name: String, number: Int, onResponse: (String) -> Unit, onFailure: (String) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("email", email)
            put("name", name)
            put("number", number)
        }

        val dataObject = JSONObject().apply {
            put("data", jsonObject.toString())
        }

        val body = dataObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("https://cmsc436-2301.cs.umd.edu/project6Write.php")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Request failed: ${e.message}")
                onFailure("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("MainActivity", "WRITE response: $responseData")
                if (response.isSuccessful) {
                    try {
                        val jsonResponse = responseData?.let { JSONObject(it) }
                        val result = jsonResponse?.getString("result") ?: "No result"
                        onResponse(result)
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
