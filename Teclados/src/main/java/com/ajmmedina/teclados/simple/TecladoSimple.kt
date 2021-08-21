package com.ajmmedina.teclados.simple

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajmmedina.teclados.compartido.HiloRepetirAccion
import com.ajmmedina.teclados.compartido.OnBotonesPresionadosTecladoSimple
import com.ajmmedina.teclados.databinding.ActividadTecladoSimpleBinding

class TecladoSimple @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    
    // Declaramos una variable para acceder a los elementos de la vista View Binding 
    private val binding = ActividadTecladoSimpleBinding.inflate(LayoutInflater.from(context), this, true)

    enum class Direccion { IZQUIERDA, DERECHA }

    private val contexto: Context = context

    // Asignamos los eventos para que estos puedan ser llamados
    private var eventos: OnBotonesPresionadosTecladoSimple? = null

    fun setOnBotonesPresionados(evento: OnBotonesPresionadosTecladoSimple) { eventos = evento}

    fun setOnBotonesPresionados(aceptar: (Double) -> Unit, cancelar: () -> Unit) {
        eventos = object: OnBotonesPresionadosTecladoSimple {

            override fun onBotonAceptar(valor: Double) { aceptar(valor) }

            override fun onBotonCancelar() { cancelar() }
        }
    }

    init {
        controlesTeclado()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun controlesTeclado() {
        // Evitamos que se active el teclado al presionar el View que está encima del Edit
        binding.ViewTapaEdit.setOnClickListener {
            // Al presionar la tapa damos el foco al Edit para mostrar el cursor
            binding.EditNumero.requestFocus()
        }

        // Agregamos el dígito presionado al Edit
        binding.Boton0.setOnClickListener { agregarDigito('0') }
        binding.Boton1.setOnClickListener { agregarDigito('1') }
        binding.Boton2.setOnClickListener { agregarDigito('2') }
        binding.Boton3.setOnClickListener { agregarDigito('3') }
        binding.Boton4.setOnClickListener { agregarDigito('4') }
        binding.Boton5.setOnClickListener { agregarDigito('5') }
        binding.Boton6.setOnClickListener { agregarDigito('6') }
        binding.Boton7.setOnClickListener { agregarDigito('7') }
        binding.Boton8.setOnClickListener { agregarDigito('8') }
        binding.Boton9.setOnClickListener { agregarDigito('9') }
        binding.BotonPunto.setOnClickListener { agregarDigito('.') }

        // Movemos el cursor
        binding.BotonMoverDerecha.setOnTouchListener(
            HiloRepetirAccion(
                400,
                100
            ) { moverCursorEdit( Direccion.DERECHA) }
        )

        binding.BotonMoverIzquierda.setOnTouchListener(
            HiloRepetirAccion(
                400,
                100
            ) { moverCursorEdit( Direccion.IZQUIERDA) }
        )

        // Borramos los dígitos
        binding.BotonBorrarNumero.setOnTouchListener(
            HiloRepetirAccion(
                400,
                100
            ) { borrarDigito() }
        )
        binding.BotonBorrarTodo.setOnClickListener { binding.EditNumero.setText("") }
        
        // Presionamos el botón aceptar
        binding.BotonOk.setOnClickListener {
            // Cogemos el texto del Edit
            val texto = binding.EditNumero.text.toString()
            // Si está vacío salimos
            if (texto.isEmpty()) {
                Toast.makeText(contexto, "Debe de escribir un dato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Convertimos el valor a double
            val valor = texto.toDouble()
            // Devolvemos el valor al evento aceptar
            eventos?.onBotonAceptar(valor)
            // Borramos el valor escrito en Edit
            binding.EditNumero.setText("")
        }
        
        // Presionamos el botón cancelar
        binding.BotonMoverAbajo.setOnClickListener {
            // Devolvemos el valor al evento cancelar
            eventos?.onBotonCancelar()
            // Borramos el valor escrito en Edit
            binding.EditNumero.setText("")
        }
    }

    private fun borrarDigito() {
        // Cogemos el texto del Edit
        var texto = binding.EditNumero.text.toString()
        // Cogemos la posición del cursor
        val posicionCursor = binding.EditNumero.selectionStart
        if (texto.isNotEmpty()) {
            // Cogemos el texto hasta antes del cursor
            val textoAntesCursor = texto.subSequence(0, posicionCursor - 1).toString()
            // Cogemos el texto después del cursor
            var textoDespuesCursor = ""
            if (posicionCursor < texto.length) textoDespuesCursor =
                texto.subSequence(posicionCursor, texto.length).toString()
            texto = textoAntesCursor + textoDespuesCursor
            // Agregamos el texto al Edit
            binding.EditNumero.setText(texto)
            // Posicionamos el cursor
            binding.EditNumero.setSelection(posicionCursor - 1)
        }
    }

    private fun moverCursorEdit(mover: Direccion) {
        // Cogemos el texto del Edit
        val texto = binding.EditNumero.text.toString()
        val longitud = texto.length
        // Cogemos la posición del cursor
        val posicionCursor = binding.EditNumero.selectionStart
        if (mover == Direccion.IZQUIERDA) {
            if (posicionCursor > 0) binding.EditNumero.setSelection(posicionCursor - 1)
        } else {
            if (posicionCursor < longitud) binding.EditNumero.setSelection(posicionCursor + 1)
        }
    }

    private fun agregarDigito(digito: Char) {
        try {
            // Cogemos el texto del Edit
            var texto = binding.EditNumero.text.toString()
            // Ubicamos la posicion del cursor
            var posicionCursor = binding.EditNumero.selectionStart
            // Longitud del texto
            val longitud = texto.length
            // Si no hay texto, agregamos el dígito
            if (texto.isNotEmpty()) {
                // Cogemos el texto hasta antes del cursor
                val textoAntesCursor = texto.subSequence(0, posicionCursor).toString()
                // Cogemos el texto después del cursor
                var textoDespuesCursor = ""
                if (posicionCursor < longitud) textoDespuesCursor =
                    texto.subSequence(posicionCursor, longitud).toString()
                if (digito == '.') {
                    // Verificamos que no este escrito ya el punto
                    val posicionPunto = texto.indexOf('.')
                    // Si no está escrito el punto lo agregamos
                    if (posicionPunto == -1) texto = textoAntesCursor + digito + textoDespuesCursor
                    else posicionCursor -= 1
                } else texto = textoAntesCursor + digito + textoDespuesCursor

            } else texto = "$digito"
            // Agregamos el texto al Edit
            binding.EditNumero.setText(texto)
            // Posicionamos el cursor al final del texto
            binding.EditNumero.setSelection(posicionCursor + 1)
        } catch (error: Exception) {
            return
        }
    }
}