package com.example.myapplication.models

data class Registro(
    val id: Int,
    val petId: Int,    // Relacionado con la mascota
    val dateIn: String, // Fecha de entrada
    val dateOut: String // Fecha de salida
)
