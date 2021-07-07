package com.example.saveit.ui.movimientos.actualizar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.saveit.R
import com.example.saveit.data.Categoria
import com.example.saveit.data.MedioPago
import com.example.saveit.data.Moneda
import com.example.saveit.data.TipoMovimiento
import com.example.saveit.databinding.ActualizarMovimientoFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.retrofit.DolarService
import com.example.saveit.retrofit.Respuesta
import com.example.saveit.viewmodel.MovimientoViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.actualizar_movimiento_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class ActualizarMovimientoFragment : Fragment() {
    private var _binding: ActualizarMovimientoFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var clienteUbicacion: FusedLocationProviderClient

    private var listener: OnFragmentInteractionListener? = null

    private val args by navArgs<ActualizarMovimientoFragmentArgs>()

    private lateinit var mMovimientoViewModel: MovimientoViewModel
    private var tipoMovimiento by Delegates.notNull<Int>()
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    private val RQ_SPEECH_REC = 102

    private var cotizacionDolar: Double = 0.0

    val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha de Movimiento")
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ActualizarMovimientoFragmentBinding.inflate(inflater, container, false)

        clienteUbicacion = LocationServices.getFusedLocationProviderClient(requireActivity())

        iniciarCamposListaDesplegable()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        binding.botonIngresoActualizar.setOnClickListener {
            tipoMovimiento = TipoMovimiento.INGRESO.valor
            agregarItemsAListasDesplegables()
        }

        binding.botonEgresoActualizar.setOnClickListener {
            tipoMovimiento = TipoMovimiento.EGRESO.valor
            agregarItemsAListasDesplegables()
        }

        setValuesToFields()

        binding.botonAceptarActualizar.setOnClickListener {
            updateMovimiento()
        }

        binding.botonCancelarActualizar.setOnClickListener {
            limpiarContenidoControles()
        }

        binding.botonVoz.setOnClickListener {
            askSpeechInput()
        }

        binding.botonUbicacion.setOnClickListener {
            Toast.makeText(this.context, "Boton Presionado: "+ binding.botonUbicacion.isPressed.toString(), Toast.LENGTH_SHORT).show()

            if(binding.botonUbicacion.isPressed) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    }
                    else {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    }
                }
                else {
                    obtenerUbicacionActual();
                }
            }
            else {
                Toast.makeText(this.context, "Boton Presionado: " + binding.botonUbicacion.isPressed.toString(), Toast.LENGTH_SHORT).show()

                longitud = 0.0
                latitud = 0.0

                Toast.makeText(this.context, "Latitud: " + latitud + "Longitud: " + longitud, Toast.LENGTH_SHORT).show()
            }
        }

        if (!tieneHardwareNecesario()) {
            binding.botonUbicacion.visibility = View.GONE
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val result: Call<Respuesta> = DolarService().getDolarValue("USD_ARS", "ultra", "175657d7d9f194e9f441")

        result.enqueue(object: Callback<Respuesta> {
            override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                cotizacionDolar = response.body()!!.USD_ARS
            }

            override fun onFailure(call: Call<Respuesta>, error: Throwable) {
                Toast.makeText(activity, "No se pudo obtener el valor del dólar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this.context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this.context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }

                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val textoVoz = result?.get(0).toString().toLowerCase()

            val tipoMovimientoVoz = getTipoMovimientoVoz(textoVoz)

            if (tipoMovimientoVoz == "ingreso") {
                if (tipoMovimiento != TipoMovimiento.INGRESO.valor) {
                    botonIngresoActualizar.performClick()
                }
            }
            else if (tipoMovimientoVoz == "gasto") {
                if (tipoMovimiento != TipoMovimiento.EGRESO.valor) {
                    botonEgresoActualizar.performClick()
                }
            }

            val monto = getMontoVoz(textoVoz)
            binding.actualizarMonto.setText(monto)

            val medioDePago = getMedioDePagoVoz(textoVoz)
            binding.actualizarMedioPago.setText(medioDePago, false)

            val categoria = getCategoriaVoz(textoVoz)
            binding.actualizarCategoria.setText(categoria, false)

            val descripcion = getDescripcionVoz(textoVoz)
            binding.actualizarDescripcion.setText(descripcion)
        }
    }

    private fun getTipoMovimientoVoz(textoVoz: String): String {
        if (textoVoz.contains("ingreso")) {
            return "ingreso"
        }
        else if (textoVoz.contains("gasto")) {
            return "gasto"
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

    private fun setValuesToFields() {
        binding.actualizarMonto.setText(args.currentMovimiento.monto.toString())
        binding.actualizarMoneda.setText(Moneda.getByValor(args.currentMovimiento.moneda), false)
        binding.actualizarMedioPago.setText(MedioPago.getByValor(args.currentMovimiento.medioDePago), false)
        binding.actualizarCategoria.setText(Categoria.getByValor(args.currentMovimiento.categoria), false)

        val fecha = formatDate(args.currentMovimiento.fecha)

        binding.actualizarFecha.setText(fecha)

        binding.actualizarDescripcion.setText(args.currentMovimiento.descripcion)

        if (args.currentMovimiento.tipoMovimiento == TipoMovimiento.INGRESO.valor) {
            binding.botonIngresoActualizar.performClick()
        }
        else {
            binding.botonEgresoActualizar.performClick()
        }
    }

    private fun tieneHardwareNecesario() = (requireActivity().packageManager.hasSystemFeature(
        PackageManager.FEATURE_LOCATION
    ) && requireActivity().packageManager.hasSystemFeature(
        PackageManager.FEATURE_LOCATION_GPS
    ) && requireActivity().packageManager.hasSystemFeature(
        PackageManager.FEATURE_LOCATION_NETWORK
    ))

    private fun limpiarContenidoControles() {
        iniciarCamposListaDesplegable()

        binding.grupoBotonesTipoMovimiento.clearChecked()
        binding.actualizarCategoria.text.clear()
        binding.actualizarCategoria.clearFocus()
        binding.actualizarMedioPago.text.clear()
        binding.actualizarMedioPago.clearFocus()
        binding.actualizarMoneda.text.clear()
        binding.actualizarMoneda.clearFocus()
        binding.actualizarMonto.setText("")
        binding.actualizarMonto.clearFocus()
        binding.actualizarDescripcion.setText("")
        binding.actualizarDescripcion.clearFocus()
        binding.actualizarFecha.setText("")
        binding.actualizarFecha.clearFocus()
    }

    private fun iniciarCamposListaDesplegable() {
        val itemInicial = listOf<String>("Sin items")

        agregarItemsALista(itemInicial, binding.medioPago.editText)
        agregarItemsALista(itemInicial, binding.moneda.editText)
        agregarItemsALista(itemInicial, binding.categoria.editText)
    }

    private fun agregarItemsAListasDesplegables() {
        val itemsMedioPago = MedioPago.values().map { it.descripcion }
        agregarItemsALista(itemsMedioPago, binding.medioPago.editText)

        val itemsCategorias = Categoria.values().map { it.descripcion }
        agregarItemsALista(itemsCategorias, binding.categoria.editText)

        val itemsMonedas = Moneda.values().map { it.descripcion }
        agregarItemsALista(itemsMonedas, binding.moneda.editText)
    }

    private fun agregarItemsALista(items: List<String>, componenteLista: EditText?) {
        val adapter = ArrayAdapter(requireContext(), R.layout.lista_items, items)
        (componenteLista as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacionActual() {
        clienteUbicacion.lastLocation
            .addOnSuccessListener { u ->
                if (u != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    latitud = u.latitude
                    longitud = u.longitude

                    Toast.makeText(this.context, "Latitud1: " + u.latitude.toString() + "Longitud1: " + u.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateMovimiento() {
        if (validateFields()) {
            var monto = binding.actualizarMonto.text.toString().toDouble()
            val moneda = Moneda.getByDescripcion(binding.moneda.editText?.text.toString()).valor

            val movimiento = Movimiento(args.currentMovimiento.id,
                monto,
                moneda,
                MedioPago.getByDescripcion(binding.medioPago.editText?.text.toString()).valor,
                Categoria.getByDescripcion(binding.categoria.editText?.text.toString()).valor,
                SimpleDateFormat("dd/MM/yyyy").parse(binding.actualizarFecha.text.toString()).time,
                binding.actualizarDescripcion.text.toString(),
                latitud,
                longitud,
                tipoMovimiento,
                args.currentMovimiento.cotizacionDolar
            )

            mMovimientoViewModel.updateMovimiento(movimiento)
            Toast.makeText(requireContext(), "El movimiento fue actualizado correctamente!", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateFields(): Boolean {
        if (binding.actualizarMonto.text.isNullOrEmpty()
            || binding.moneda.editText?.text.isNullOrEmpty()
            || binding.medioPago.editText?.text.isNullOrEmpty()
            || binding.categoria.editText?.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Por favor, selecciona todos los campos", Toast.LENGTH_LONG).show()

            return false
        }

        return true
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker.addOnPositiveButtonClickListener {
            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("MMM dd, yyyy").parse(datePicker.headerText)).toString())
        }

        binding.actualizarFecha.setOnClickListener {
            onFechaMovimientoPressed()
        }

        binding.actualizarMoneda.isEnabled = false
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