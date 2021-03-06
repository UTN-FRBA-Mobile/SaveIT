package com.example.saveit.ui.movimientos.agregar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.saveit.MainActivity
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.databinding.AgregarMovimientosFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.retrofit.DolarService
import com.example.saveit.retrofit.Respuesta
import com.example.saveit.viewmodel.MovimientoViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates


class AgregarMovimientosFragment : Fragment() {
    private var _binding: AgregarMovimientosFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var clienteUbicacion: FusedLocationProviderClient
    private var tokenDeCancelacion = CancellationTokenSource()
    private var listener: OnFragmentInteractionListener? = null

    private val args by navArgs<AgregarMovimientosFragmentArgs>()

    private lateinit var mMovimientoViewModel: MovimientoViewModel
    private var tipoMovimiento by Delegates.notNull<Int>()
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    private var botonIngresoFueClickeado: Boolean = false
    private var botonEgresoFueClickeado: Boolean = false
    private lateinit var fragmentPrevio: String
    private val RQ_SPEECH_REC = 102

    private var cotizacionDolar: Double = 0.0

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Fecha de Movimiento")
        .build()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AgregarMovimientosFragmentBinding.inflate(inflater, container, false)
        clienteUbicacion = LocationServices.getFusedLocationProviderClient(requireActivity())
        fragmentPrevio =
            findNavController().previousBackStackEntry?.destination?.displayName.toString()
        iniciarCamposListaDesplegable()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        binding.botonIngreso.setOnClickListener {
            tipoMovimiento = TipoMovimiento.INGRESO.valor
            botonIngresoFueClickeado = true
            botonEgresoFueClickeado = false

            agregarItemsAListasDesplegables()
        }
        binding.tipoMoneda.setOnItemClickListener { _, _, _, _ ->
            modificarTipoMonedaSegunSeleccion()
        }

        binding.botonEgreso.setOnClickListener {
            tipoMovimiento = TipoMovimiento.EGRESO.valor
            botonEgresoFueClickeado = true
            botonIngresoFueClickeado = false

            agregarItemsAListasDesplegables()
        }
        if (fragmentPrevioEsListaMovimientos()) {
            (requireActivity() as MainActivity).supportActionBar?.title = "Actualizar Movimiento"
            setValuesToFields()
        }

        binding.botonAceptar.setOnClickListener {
            insertDataToDataBase()
        }

        binding.botonCancelar.setOnClickListener {
            limpiarContenidoControles()
        }

        binding.botonVoz.setOnClickListener {
            askSpeechInput()
        }

        binding.botonUbicacion.setOnClickListener {
            if ((binding.botonUbicacion as MaterialButton).isChecked) {
                when {
                    ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        obtenerUbicacionActual();
                    }
                    shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        val alertBuilder: AlertDialog.Builder =
                            AlertDialog.Builder(requireContext())
                        alertBuilder.setCancelable(true)
                        alertBuilder.setIcon(R.drawable.outline_location_on_black_48)
                        alertBuilder.setTitle("SaveIT necesita el permiso de ubicaci??n ")
                        alertBuilder.setMessage("Para ver la distribuci??n geogr??fica de tus movimientos, en los reportes.")
                        alertBuilder.setPositiveButton(android.R.string.yes,
                            DialogInterface.OnClickListener { dialog, which ->
                                requestPermissions(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                                )
                            })

                        val alert: AlertDialog = alertBuilder.create()
                        alert.show()

                    }
                    else -> {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                        )
                    }
                }
            } else {
                limpiarUbicacion()
            }
        }

        return binding.root
    }

    private fun fragmentPrevioEsListaMovimientos(): Boolean {
        return fragmentPrevio.contains("listaMovimientosFragment")
    }

    private fun modificarTipoMonedaSegunSeleccion() {
        if (!(binding.moneda.editText?.text.toString().isEmpty())) {
            binding.moneda.startIconDrawable = null
        } else {
            binding.moneda.startIconDrawable =
                requireContext().getDrawable(R.drawable.outline_paid_black_20)
        }
    }

    private fun limpiarUbicacion() {
        (binding.botonUbicacion as MaterialButton).isChecked = false
        longitud = 0.0
        latitud = 0.0
    }

    override fun onStart() {
        super.onStart()

        val result: Call<Respuesta> =
            DolarService().getDolarValue("USD_ARS", "ultra", "175657d7d9f194e9f441")

        result.enqueue(object : Callback<Respuesta> {
            override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                when {
                    response.raw().code() != 200 -> {
                        mMovimientoViewModel.readUltimaCotizacion().observe(viewLifecycleOwner, { cotizacion ->
                            if (cotizacion != null) {
                                cotizacionDolar = cotizacion
                            }
                            else {
                                cotizacionDolar = 95.0
                            }
                        })
                    }
                    response.raw().code() == 200 -> {
                        cotizacionDolar = response.body()!!.USD_ARS
                    }
                }
            }

            override fun onFailure(call: Call<Respuesta>, error: Throwable) {

                Toast.makeText(
                    activity,
                    "No se pudo obtener el valor del d??lar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (!(ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) === PackageManager.PERMISSION_GRANTED)
                    ) {
                        (binding.botonUbicacion as MaterialButton).isChecked = false
                    }
                } else {
                    (binding.botonUbicacion as MaterialButton).isChecked = false
                }
                return
            }
        }
    }

    private fun setValuesToFields() {
        binding.monto.setText(args.currentMovimiento.monto.toString())
        binding.tipoMoneda.setText(Moneda.getByValor(args.currentMovimiento.moneda), false)
        binding.medioPagoTexto.setText(
            MedioPago.getByValor(args.currentMovimiento.medioDePago),
            false
        )

        val fecha = formatDate(args.currentMovimiento.fecha)

        binding.fecha.setText(fecha)

        binding.descripcion.setText(args.currentMovimiento.descripcion)

        if (args.currentMovimiento.tipoMovimiento == TipoMovimiento.INGRESO.valor) {
            binding.botonIngreso.performClick()

            binding.categoriaTexto.setText(
                CategoriasIngreso.getByValor(args.currentMovimiento.categoria),
                false
            )
        } else {
            binding.botonEgreso.performClick()

            binding.categoriaTexto.setText(
                CategoriasGasto.getByValor(args.currentMovimiento.categoria),
                false
            )
        }
    }

    private fun formatDate(fecha: Long): String {
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val textoVoz = result?.get(0).toString().toLowerCase()

            val tipoMovimientoVoz = getTipoMovimientoVoz(textoVoz)

            if (tipoMovimientoVoz == "ingreso") {
                if (!botonIngresoFueClickeado) {
                    binding.botonIngreso.performClick()
                }
            } else if (tipoMovimientoVoz == "gasto") {
                if (!botonEgresoFueClickeado) {
                    binding.botonEgreso.performClick()
                }
            }

            val monto = getMontoVoz(textoVoz)
            binding.monto.setText(monto)

            val medioDePago = getMedioDePagoVoz(textoVoz)
            binding.medioPagoTexto.setText(medioDePago, false)

            val categoria = getCategoriaVoz(textoVoz)
            binding.categoriaTexto.setText(categoria, false)

            val descripcion = getDescripcionVoz(textoVoz)
            binding.descripcion.setText(descripcion)
        }
    }

    private fun getTipoMovimientoVoz(textoVoz: String): String {
        if (textoVoz.contains("ingreso")) {
            return "ingreso"
        } else if (textoVoz.contains("gasto")) {
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
        MedioPago.values().forEach { m ->
            if (textoVoz.contains(m.descripcion.toLowerCase())) {
                return m.descripcion
            }
        }

        return ""
    }

    private fun getCategoriaVoz(textoVoz: String): String {
        if (tipoMovimiento == TipoMovimiento.INGRESO.valor) {
            CategoriasIngreso.values().forEach { m ->
                if (textoVoz.contains(m.descripcion.toLowerCase())) {
                    return m.descripcion
                }
            }
        } else {
            CategoriasGasto.values().forEach { m ->
                if (textoVoz.contains(m.descripcion.toLowerCase())) {
                    return m.descripcion
                }
            }
        }

        return ""
    }

    private fun getDescripcionVoz(textoVoz: String): String {
        var listaPalabrasDescripcion = textoVoz.split("descripci??n")

        if (listaPalabrasDescripcion.size > 1) {
            var descripcionReturn = listaPalabrasDescripcion[1].trim()

            return descripcionReturn.capitalize()
        }

        return ""
    }

    private fun limpiarContenidoControles() {
        iniciarCamposListaDesplegable()
        binding.grupoBotonesTipoMovimiento.clearChecked()
        binding.categoriaTexto.text.clear()
        binding.categoriaTexto.clearFocus()
        binding.medioPagoTexto.text.clear()
        binding.medioPagoTexto.clearFocus()
        binding.tipoMoneda.text.clear()
        modificarTipoMonedaSegunSeleccion()
        binding.tipoMoneda.clearFocus()
        binding.monto.setText("")
        binding.monto.clearFocus()
        binding.descripcion.setText("")
        binding.descripcion.clearFocus()
        binding.fecha.setText("")
        binding.fecha.clearFocus()
        limpiarUbicacion()
        if (fragmentPrevioEsListaMovimientos()) {
            findNavController().previousBackStackEntry?.destination?.let {
                findNavController().navigate(
                    it.id
                )
            }
        }
    }

    private fun iniciarCamposListaDesplegable() {
        val itemInicial = listOf<String>()
        agregarItemsALista(itemInicial, binding.medioPago.editText)
        agregarItemsALista(itemInicial, binding.moneda.editText)
        agregarItemsALista(itemInicial, binding.categoria.editText)
    }

    private fun agregarItemsAListasDesplegables() {
        val itemsMedioPago = MedioPago.values().map { it.descripcion }
        agregarItemsALista(itemsMedioPago, binding.medioPago.editText)

        val itemsCategorias =
            if (tipoMovimiento == TipoMovimiento.INGRESO.valor) CategoriasIngreso.values()
                .map { it.descripcion } else CategoriasGasto.values().map { it.descripcion }
        binding.categoriaTexto.text.clear()
        binding.categoriaTexto.clearFocus()
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
        val currentLocationTask: Task<Location> = clienteUbicacion.getCurrentLocation(PRIORITY_HIGH_ACCURACY, tokenDeCancelacion.token)
        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful) {
                latitud = task.result.latitude
                longitud = task.result.longitude
            } else {
                val exception = task.exception
                "Location (failure): $exception"
                Toast.makeText(
                    requireContext(),
                    "No se pudo obtener la ubicaci??n.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun insertDataToDataBase() {
        if (validateFields()) {
            var id: Int

            var monto = binding.monto.text.toString().toDouble()
            val moneda = Moneda.getByDescripcion(binding.moneda.editText?.text.toString()).valor
            var categoria = 0

            if (fragmentPrevioEsListaMovimientos()) {
                id = args.currentMovimiento.id
                latitud = args.currentMovimiento.latitud
                longitud = args.currentMovimiento.longitud
                cotizacionDolar = args.currentMovimiento.cotizacionDolar
            } else {
                id = 0
                if (moneda == Moneda.PESO.valor) {
                    cotizacionDolar = 1.0
                }
            }

            if (tipoMovimiento == TipoMovimiento.INGRESO.valor) {
                categoria =
                    CategoriasIngreso.getByDescripcion(binding.categoria.editText?.text.toString()).valor
            } else {
                categoria =
                    CategoriasGasto.getByDescripcion(binding.categoria.editText?.text.toString()).valor
            }

            val movimiento = Movimiento(
                id,
                monto,
                moneda,
                MedioPago.getByDescripcion(binding.medioPago.editText?.text.toString()).valor,
                categoria,
                SimpleDateFormat("dd/MM/yyyy").parse(binding.fecha.text.toString()).time,
                binding.descripcion.text.toString(),
                latitud,
                longitud,
                tipoMovimiento,
                cotizacionDolar
            )

            // Add Data to Database
            if (!fragmentPrevioEsListaMovimientos()) {
                mMovimientoViewModel.addMovimiento(movimiento)
                Toast.makeText(
                    requireContext(),
                    "El movimiento fue creado correctamente!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                mMovimientoViewModel.updateMovimiento(movimiento)
                Toast.makeText(
                    requireContext(),
                    "El movimiento fue actualizado correctamente!",
                    Toast.LENGTH_LONG
                ).show()
            }
            limpiarContenidoControles()
        }
    }

    private fun validateFields(): Boolean {
        if (binding.monto.text.isNullOrEmpty()
            || binding.moneda.editText?.text.isNullOrEmpty()
            || binding.medioPago.editText?.text.isNullOrEmpty()
            || binding.categoria.editText?.text.isNullOrEmpty()
            || binding.fechaMovimiento.editText?.text.isNullOrEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                "Por favor, selecciona todos los campos",
                Toast.LENGTH_LONG
            ).show()

            return false
        }

        return true
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                "El reconocimiento de voz no est?? disponible",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            i.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "El formato es: 'Ingreso/Gasto de X pesos con medio de pago X con categor??a X y descripci??n X'"
            )

            startActivityForResult(i, RQ_SPEECH_REC)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker.addOnPositiveButtonClickListener {
            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(
                SimpleDateFormat("dd/MM/yyyy").format(
                    SimpleDateFormat("MMM dd, yyyy").parse(datePicker.headerText)
                ).toString()
            )
        }

        binding.fecha.setOnClickListener {
            onFechaMovimientoPressed()
        }
        if (fragmentPrevioEsListaMovimientos()) {
            binding.moneda.isEnabled = false
            binding.botonUbicacion.isEnabled = false
        }
    }

    fun onFechaMovimientoPressed() {
        if(!datePicker.isVisible) {
            datePicker.show((activity as AppCompatActivity).supportFragmentManager, "tag")
        }
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
            AgregarMovimientosFragment()
    }
}