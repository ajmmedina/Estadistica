package com.ajmmedina.estadistica.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ajmmedina.estadistica.databinding.ActividadPrincipalBinding
import com.ajmmedina.teclados.compartido.OnBotonesPresionadosTecladoCompuesto
import com.ajmmedina.teclados.compartido.OnBotonesPresionadosTecladoSimple

class ActividadPrincipal : AppCompatActivity() {

    private lateinit var binding: ActividadPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActividadPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        crearTeclado()
        botones()
    }

    private fun crearTeclado() {
        binding.tecladoSimple.setOnBotonesPresionados(object : OnBotonesPresionadosTecladoSimple {
            override fun onBotonAceptar(valor: Double) {
                Toast.makeText(this@ActividadPrincipal, "Valor = $valor", Toast.LENGTH_SHORT).show()
            }

            override fun onBotonCancelar() {
                binding.guideSeparacion.setGuidelinePercent(1F)
            }

        })
    }

    private fun botones() {
        binding.botonDatoAgregar.setOnClickListener {
            binding.guideSeparacion.setGuidelinePercent(0.5F)
        }
    }
}