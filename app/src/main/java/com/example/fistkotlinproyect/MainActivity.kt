package com.example.fistkotlinproyect

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fistkotlinproyect.ui.theme.FistKotlinProyectTheme
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : ComponentActivity() {
    private val sharedPref: SharedPreferences by lazy {
        this.getSharedPreferences("my_custom_file", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val keyInput: EditText = findViewById(R.id.keyInput)
        val valueInput: EditText = findViewById(R.id.valueInput)
        val saveButton: Button = findViewById(R.id.saveButton)
        val retrieveButton: Button = findViewById(R.id.retrieveButton)
        val displayText: TextView = findViewById(R.id.displayText)

        saveButton.setOnClickListener {
            val key = keyInput.text.toString()
            val value = valueInput.text.toString()

            if (key.isNotEmpty() && value.isNotEmpty()) {
                sharedPref.edit().putString(key, value).apply()
                Toast.makeText(this, "Key-Value saved!", Toast.LENGTH_SHORT).show()
//                keyInput.setText("")
                valueInput.setText("")
            } else {
                Toast.makeText(this, "Please enter both key and value", Toast.LENGTH_SHORT).show()
            }
        }

        // Retrieve the value when the "Retrieve" button is clicked
        retrieveButton.setOnClickListener {
            val key = keyInput.text.toString()

            if (key.isNotEmpty()) {
                val savedValue = sharedPref.getString(key, "No value found")
                displayText.text = savedValue
            } else {
                Toast.makeText(this, "Please enter a key to retrieve", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Save the key-value pair when the "Save" button is clicked

}

