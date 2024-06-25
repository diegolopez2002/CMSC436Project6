package com.example.project6

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonWrite: Button = findViewById(R.id.writebutton)
        val buttonRead: Button = findViewById(R.id.readbutton)
        textView = findViewById(R.id.tv)

        buttonWrite.setOnClickListener {
            Project6Write().sendJsonRequest(
                email = "your_email@example.com",
                name = "Some Name",
                number = 123,
                onResponse = { response ->
                    runOnUiThread {
                        textView.text = response
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        textView.text = error
                    }
                }
            )
        }

        buttonRead.setOnClickListener {
            Project6Read().readData(
                email = "your_email@example.com",
                onResponse = { response, number ->
                    runOnUiThread {
                        textView.text = response
                        setTextViewBackgroundColor(number)
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        textView.text = error
                    }
                }
            )
        }
    }

    private fun setTextViewBackgroundColor(number: Int) {
        val color = when (number) {
            20 -> Color.RED
            41 -> Color.BLUE
            67 -> Color.GREEN
            23 -> Color.YELLOW
            19 -> Color.CYAN
            else -> Color.WHITE
        }
        textView.setBackgroundColor(color)
    }
}
