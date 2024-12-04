package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        // Referencias a vistas
        val ownerName = findViewById<EditText>(R.id.editOwnerName)
        val ownerContact = findViewById<EditText>(R.id.editOwnerContact)
        val petName = findViewById<EditText>(R.id.editPetName)
        val petType = findViewById<EditText>(R.id.editPetType)
        val petAge = findViewById<EditText>(R.id.editPetAge)
        val btnSave = findViewById<Button>(R.id.btnSaveData)

        // Acción del botón
        btnSave.setOnClickListener {
            val ownerId = dbHelper.addOwner(
                ownerName.text.toString(),
                ownerContact.text.toString()
            )
            Toast.makeText(this, "Dueño guardado con ID: $ownerId", Toast.LENGTH_SHORT).show()

            class MainActivity : AppCompatActivity() {
                private lateinit var imageViewRandomAnimal: ImageView
                private lateinit var btnRandomAnimal: Button

                // Array de imágenes locales en drawable
                private val animalImages = arrayOf(
                    R.drawable.animal1,
                    R.drawable.animal2,
                    R.drawable.animal3,
                    R.drawable.animal4
                )

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_main)

                    imageViewRandomAnimal = findViewById(R.id.imageViewRandomAnimal)
                    btnRandomAnimal = findViewById(R.id.btnRandomAnimal)

                    // Cargar una imagen inicial aleatoria
                    loadRandomImage()

                    btnRandomAnimal.setOnClickListener {
                        loadRandomImage()
                    }
                }

                private fun loadRandomImage() {
                    val randomIndex = Random.nextInt(animalImages.size)
                    imageViewRandomAnimal.setImageResource(animalImages[randomIndex])
                }
            }
        }
    }
}
