package com.example.saveit.ui.movimientos.actualizar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.saveit.R
import com.example.saveit.data.Categoria
import com.example.saveit.data.MedioPago
import com.example.saveit.data.Moneda
import com.example.saveit.databinding.ActualizarMovimientoFragmentBinding
import com.example.saveit.viewmodel.MovimientoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.actualizar_movimiento_fragment.*
import kotlinx.android.synthetic.main.ahorro_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class ActualizarMovimientoFragment : Fragment() {
    private var _binding: ActualizarMovimientoFragmentBinding? = null
    private val binding get() = _binding!!

    private var listener: OnFragmentInteractionListener? = null

    private val args by navArgs<ActualizarMovimientoFragmentArgs>()

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    private val RQ_SPEECH_REC = 102

    val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha de Movimiento")
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ActualizarMovimientoFragmentBinding.inflate(inflater, container, false)

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        setAutoCompleteTextViews()

        binding.actualizarMonto.setText(args.currentMovimiento.monto.toString())
        binding.moneda.editText!!.setText(Moneda.getByValor(args.currentMovimiento.moneda))
        binding.medioPago.editText!!.setText(MedioPago.getByValor(args.currentMovimiento.medioDePago))
        binding.categoria.editText!!.setText(Categoria.getByValor(args.currentMovimiento.categoria))

        val fecha = formatDate(args.currentMovimiento.fecha)

        binding.actualizarFecha.setText(fecha)

        binding.actualizarDescripcion.setText(args.currentMovimiento.descripcion)

        binding.botonActualizar.setOnClickListener {
            updateMovimiento()
        }

        binding.botonVoz.setOnClickListener {
            askSpeechInput()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val textoVoz = result?.get(0).toString().toLowerCase()

            val tipoMovimiento = getTipoMovimientoVoz(textoVoz)

            val monto = getMontoVoz(textoVoz)
            binding.actualizarMonto.setText(monto)

            val medioDePago = getMedioDePagoVoz(textoVoz)
            binding.medioPago.editText!!.setText(medioDePago)

            val categoria = getCategoriaVoz(textoVoz)
            binding.categoria.editText!!.setText(categoria)

            val descripcion = getDescripcionVoz(textoVoz)
            binding.actualizarDescripcion.setText(descripcion)
        }
    }

    private fun getTipoMovimientoVoz(textoVoz: String): String {
        if (textoVoz.contains("ingreso")) {
            return "ingreso"
        }
        else if (textoVoz.contains("salida")) {
            return "salida"
        }

        return ""
    }

    private fun getMontoVoz(textoVoz: String): String {
        val listaPalabrasMontoVoz = textoVoz.split(" ").toList()

        var monto = listaPalabrasMontoVoz.filter { x -> x.contains("$") }

        if (monto.isNotEmpty()) {
            return monto.first().split("$")[1]
        }

        return ""
    }

    private fun getMedioDePagoVoz(textoVoz: String): String {
        MedioPago.values().forEach { m -> if (textoVoz.contains(m.descripcion.toLowerCase())) { return m.descripcion } }

        return ""
    }

    private fun getCategoriaVoz(textoVoz: String): String {
        Categoria.values().forEach { m -> if (textoVoz.contains(m.descripcion.toLowerCase())) { return m.descripcion } }

        return ""
    }

    private fun getDescripcionVoz(textoVoz: String): String {
        var listaPalabrasDescripcion = textoVoz.split("descripción")

        if (listaPalabrasDescripcion.size > 1) {
            var descripcionReturn = listaPalabrasDescripcion[1].trim()

            return descripcionReturn.capitalize()
        }

        return ""
    }

    private fun setAutoCompleteTextViews() {
        val itemInicial = listOf<String>("Sin Medio Pago")

        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
        (binding.medioPago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

        val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

        val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
        (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)

        binding.botonIngreso.setOnClickListener {
            val itemsMedioPago = MedioPago.values().map { it.descripcion }
            val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.medioPago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

            val itemsCategorias = Categoria.values().map { it.descripcion }
            val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

            val itemsMonedas = Moneda.values().map { it.descripcion }
            val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)
        }

        binding.botonEgreso.setOnClickListener {
            val itemsMedioPago = MedioPago.values().map { it.descripcion }
            val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.medioPago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

            val itemsCategorias = Categoria.values().map { it.descripcion }
            val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

            val itemsMonedas = Moneda.values().map { it.descripcion }
            val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
            (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)
        }
    }

    private fun updateMovimiento() {
        /*val firstName = updateFirstName_et.text.toString()
        val lastName = updateLastName_et.text.toString()
        val age = updateAge_et.text

        if (inputCheck(firstName, lastName, age)) {
//            val updatedMovimiento = Movimiento(args.currentMovimiento.id, firstName.toDouble(), firstName.toInt(), firstName.toInt(), Date().time, firstName, firstName, firstName.toInt())

//            mMovimientoViewModel.updateMovimiento(updatedMovimiento)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_actualizarMovimientoFragment_to_listaMovimientosFragment2)
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }*/
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            Toast.makeText(requireContext(), "El reconocimiento de voz no está disponible", Toast.LENGTH_LONG).show()
        }
        else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "El formato es: 'Ingreso/Gasto de X pesos con medio de pago X con categoría X y descripción X'")

            startActivityForResult(i, RQ_SPEECH_REC)
        }
    }

    private fun formatDate(fecha: Long): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker.addOnPositiveButtonClickListener {
            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(android.icu.text.SimpleDateFormat("dd/MM/yyyy").format(android.icu.text.SimpleDateFormat("MMM dd, yyyy").parse(datePicker.headerText)).toString())
        }

        binding.actualizarFecha.setOnClickListener {
            onFechaMovimientoPressed()
        }
    }

    private fun onFechaMovimientoPressed() {
        datePicker.show((activity as AppCompatActivity).supportFragmentManager , "tag")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment StatusUpdate.
         */
        @JvmStatic
        fun newInstance() =
            ActualizarMovimientoFragment()
    }
}