package com.example.fistkotlinproyect

import com.example.fistkotlinproyect.database.DatabaseHelper
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : ComponentActivity() {
    private val sharedPref: SharedPreferences by lazy {
        this.getSharedPreferences("my_custom_file", Context.MODE_PRIVATE)
    }

    // Instancier DatabaseHelper
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Initialiser DatabaseHelper
        dbHelper = DatabaseHelper(this)

        val keyInput: EditText = findViewById(R.id.keyInput)
        val valueInput: EditText = findViewById(R.id.valueInput)
        val saveButton: Button = findViewById(R.id.saveButton)
        val retrieveButton: Button = findViewById(R.id.retrieveButton)
        val displayText: TextView = findViewById(R.id.displayText)

        // pour la base de données
        val etName: EditText = findViewById(R.id.etName)
        val etAge: EditText = findViewById(R.id.etAge)
        val btnAddUser: Button = findViewById(R.id.btnAddUser)
        val btnShowUsers: Button = findViewById(R.id.btnShowUsers)
        val tvUsers: TextView = findViewById(R.id.tvUsers)
        val btnDeleteUser: Button = findViewById(R.id.btnDeleteUser)
        val etUserIdToDelete: EditText = findViewById(R.id.etUserIdToDelete)

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
        // Save the key-value pair when the "Save" button is clicked

        // Bouton pour ajouter un utilisateur
        btnAddUser.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toIntOrNull()

            if (name.isNotEmpty() && age != null) {
                // Insérer l'utilisateur dans la base de données
                dbHelper.insertUser(name, age)
                Toast.makeText(this, "User added!", Toast.LENGTH_SHORT).show()

                // Réinitialiser les champs de saisie
                etName.text.clear()
                etAge.text.clear()
            } else {
                Toast.makeText(this, "Please enter a valid name and age", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton pour supprimer un utilisateur
        btnDeleteUser.setOnClickListener {
            val userId = etUserIdToDelete.text.toString().toIntOrNull()

            if (userId != null) {
                val deletedRows = dbHelper.deleteUser(userId)
                if (deletedRows > 0) {
                    Toast.makeText(this, "User deleted!", Toast.LENGTH_SHORT).show()
                    etUserIdToDelete.text.clear()
                } else {
                    Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid User ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton pour afficher tous les utilisateurs
        btnShowUsers.setOnClickListener {
            val users = dbHelper.getAllUsers()

            if (users.isNotEmpty()) {
                // Afficher la liste des utilisateurs dans le TextView
                val userText = users.joinToString("\n") { user ->
                    "ID: ${user.id}, Name: ${user.name}, Age: ${user.age}"
                }
                tvUsers.text = userText
            } else {
                tvUsers.text = "No users found"
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close() // Fermer la connexion à la base de données
        super.onDestroy()
    }
}

