package com.stomas.veterinarioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException

class MainActivity : AppCompatActivity() {
    private var citasRef: DatabaseReference? = null
    private var mqttClient: MqttAndroidClient? = null

    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.android.tools.r8.graph.T.R.layout.activity_main)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        citasRef = database.getReference("citas")

        val broker = "tcp://broker.hivemq.com:1883" // Cambia si usas otro bróker
        val clientId: String = MqttClient.generateClientId()
        mqttClient = MqttAndroidClient(getApplicationContext(), broker, clientId)

        try {
            val options: MqttConnectOptions = MqttConnectOptions()
            options.setCleanSession(true)
            mqttClient.connect(options)
        } catch (e: MqttException) {
            e.printStackTrace()
        }

        val etCliente: EditText = findViewById(com.android.tools.r8.graph.T.R.id.et_cliente)
        val etMascota: EditText = findViewById(com.android.tools.r8.graph.T.R.id.et_mascota)
        val etDetalle: EditText = findViewById(com.android.tools.r8.graph.T.R.id.et_detalle)
        val btnGuardar: Button = findViewById(com.android.tools.r8.graph.T.R.id.btn_guardar)
        val btnEnviarMQTT: Button = findViewById(com.android.tools.r8.graph.T.R.id.btn_enviar_mqtt)

        btnGuardar.setOnClickListener { view ->
            val cliente: String = etCliente.getText().toString()
            val mascota: String = etMascota.getText().toString()
            val detalle: String = etDetalle.getText().toString()

            if (cliente.isEmpty() || mascota.isEmpty() || detalle.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Por favor, completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val cita: java.util.HashMap<String, String> =
                java.util.HashMap<String, String>()
            cita.put("cliente", cliente)
            cita.put("mascota", mascota)
            cita.put("detalle", detalle)
            citasRef.push().setValue(cita).addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Datos guardados en Firebase",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al guardar datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        btnEnviarMQTT.setOnClickListener { view ->
            val mensaje =
                ("Nueva cita: Cliente - " + etCliente.getText().toString()).toString() +
                        ", Mascota - " + etMascota.getText().toString()
            try {
                mqttClient.publish("veterinario/citas", mensaje.toByteArray(), 0, false)
                Toast.makeText(
                    this@MainActivity,
                    "Mensaje enviado al bróker MQTT",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: MqttException) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Error al enviar mensaje MQTT",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}