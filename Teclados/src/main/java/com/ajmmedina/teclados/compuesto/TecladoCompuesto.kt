package com.ajmmedina.teclados.compuesto

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajmmedina.teclados.compartido.HiloRepetirAccion
import com.ajmmedina.teclados.compartido.OnBotonesPresionadosTecladoCompuesto
import com.ajmmedina.teclados.databinding.ActividadTecladoCompuestoBinding

class TecladoCompuesto @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ActividadTecladoCompuestoBinding.inflate(LayoutInflater.from(context), this, true)

    enum class Direccion { IZQUIERDA, DERECHA }

    // Declaramos un Edit global
    private var editNumero: EditText
    private val contexto: Context = context

    // Asignamos los eventos para que estos puedan ser llamados
    private var eventos: OnBotonesPresionadosTecladoCompuesto? = null

    fun setOnBotonesPresionados(evento: OnBotonesPresionadosTecladoCompuesto) { eventos = evento}

    fun setOnBotonesPresionados(aceptar: (Double, Double) -> Unit, cancelar: () -> Unit) {
        eventos = object: OnBotonesPresionadosTecladoCompuesto {

            override fun onBotonAceptar(valorX: Double, valorY: Double) { aceptar(valorX, valorY) }

            override fun onBotonCancelar() { cancelar() }
        }
    }

    init {
        // Damos acceso al Edit X
        editNumero = binding.EditNumeroX
        // Damos acceso a todos los métodos
        controlesTeclado()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun controlesTeclado() {
        // Evitamos que se active el teclado al presionar el View que está encima del Edit
        binding.ViewTapaEditX.setOnClickListener {
            // Al presionar la tapa damos el foco al Edit para mostrar el cursor
            val texto = binding.EditNumeroX.text.toString()
            if (texto.isNotEmpty()) binding.EditNumeroX.setSelection(texto.length)
            binding.EditNumeroX.requestFocus()
            editNumero = binding.EditNumeroX
        }

        binding.ViewTapaEditY.setOnClickListener {
            // Al presionar la tapa damos el foco al Edit para mostrar el cursor
            val texto = binding.EditNumeroY.text.toString()
            if (texto.isNotEmpty()) binding.EditNumeroY.setSelection(texto.length)
            binding.EditNumeroY.requestFocus()
            editNumero = binding.EditNumeroY
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

        // Presionamos el botón aceptar
        binding.BotonOk.setOnClickListener {
            // Cogemos el texto del Edit
            val textoX = binding.EditNumeroX.text.toString()
            val textoY = binding.EditNumeroY.text.toString()
            // Si está vacío salimos
            if (textoX.isEmpty() || textoY.isEmpty()) {
                val texto = if (textoX.isEmpty() && textoY.isNotEmpty()) "Dato X no puede quedar vacío"
                else if (textoX.isNotEmpty() && textoY.isEmpty()) "Dato Y no puede quedar vacío"
                else "Debe escribir los datos en \"Datos X\" y \"Datos Y\""
                Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
                binding.EditNumeroX.requestFocus()
                editNumero = binding.EditNumeroX
                return@setOnClickListener
            }
            // Convertimos el valor a double
            val valorX = textoX.toDouble()
            val valorY = textoY.toDouble()
            // Devolvemos el valor al evento aceptar
            eventos?.onBotonAceptar(valorX, valorY)
            // Borramos el valor escrito en Edit
            binding.EditNumeroX.setText("")
            binding.EditNumeroY.setText("")
            binding.EditNumeroX.requestFocus()
            editNumero = binding.EditNumeroX
        }

        // Presionamos el botón cancelar
        binding.BotonMoverAbajo.setOnClickListener {
            // Devolvemos el valor al evento cancelar
            eventos?.onBotonCancelar()
            // Borramos el valor escrito en Edit
            binding.EditNumeroX.setText("")
            binding.EditNumeroY.setText("")
            // Damos el foco el Edit X
            binding.EditNumeroX.requestFocus()
        }

        // Presionamos el botón intercambiar
        binding.BotonIntercambiarEdit.setOnClickListener {
            editNumero = if (editNumero == binding.EditNumeroX) {
                val texto = binding.EditNumeroY.text.toString()
                if (texto.isNotEmpty()) binding.EditNumeroY.setSelection(texto.length)
                binding.EditNumeroY.requestFocus()
                binding.EditNumeroY
            } else {
                val texto = binding.EditNumeroX.text.toString()
                if (texto.isNotEmpty()) binding.EditNumeroX.setSelection(texto.length)
                binding.EditNumeroX.requestFocus()
                binding.EditNumeroX
            }
        }
    }

    private fun borrarDigito() {
        // Cogemos el texto del Edit
        var texto = editNumero.text.toString()
        // Cogemos la posición del cursor
        val posicionCursor = editNumero.selectionStart
        if (texto.isNotEmpty()) {
            // Cogemos el texto hasta antes del cursor
            val textoAntesCursor = texto.subSequence(0, posicionCursor - 1).toString()
            // Cogemos el texto después del cursor
            var textoDespuesCursor = ""
            if (posicionCursor < texto.length) textoDespuesCursor =
                texto.subSequence(posicionCursor, texto.length).toString()
            texto = textoAntesCursor + textoDespuesCursor
            // Agregamos el texto al Edit
            editNumero.setText(texto)
            // Posicionamos el cursor
            editNumero.setSelection(posicionCursor - 1)
        }
    }

    private fun moverCursorEdit(mover: Direccion) {
        // Cogemos el texto del Edit
        val texto = editNumero.text.toString()
        val longitud = texto.length
        // Cogemos la posición del cursor
        val posicionCursor = editNumero.selectionStart
        if (mover == Direccion.IZQUIERDA) {
            if (posicionCursor > 0) editNumero.setSelection(posicionCursor - 1)
        } else {
            if (posicionCursor < longitud) editNumero.setSelection(posicionCursor + 1)
        }
    }

    private fun agregarDigito(digito: Char) {
        try {
            // Cogemos el texto del Edit
            var texto = editNumero.text.toString()
            // Ubicamos la posicion del cursor
            var posicionCursor = editNumero.selectionStart
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
            editNumero.setText(texto)
            // Posicionamos el cursor al final del texto
            editNumero.setSelection(posicionCursor + 1)
        } catch (error: Exception) {
            return
        }
    }
}