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

        crearTeclados()
    }

    private fun crearTeclados() {
        binding.tecladoSimple.setOnBotonesPresionados(object : OnBotonesPresionadosTecladoSimple {
            override fun onBotonAceptar(valor: Double) {
                Toast.makeText(this@ActividadPrincipal, "Valor = $valor", Toast.LENGTH_SHORT).show()
            }

            override fun onBotonCancelar() {
                Toast.makeText(this@ActividadPrincipal, "Se canceló", Toast.LENGTH_SHORT).show()
            }

        })

        binding.tecladoCompuesto.setOnBotonesPresionados(object : OnBotonesPresionadosTecladoCompuesto {
            override fun onBotonAceptar(valorX: Double, valorY: Double) {
                Toast.makeText(this@ActividadPrincipal, "X = $valorX\nY = $valorY", Toast.LENGTH_SHORT).show()
            }

            override fun onBotonCancelar() {
                Toast.makeText(this@ActividadPrincipal, "Se canceló", Toast.LENGTH_SHORT).show()
            }

        })
    }
}