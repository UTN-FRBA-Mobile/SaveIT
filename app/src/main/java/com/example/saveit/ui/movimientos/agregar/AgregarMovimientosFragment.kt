package com.example.saveit.ui.movimientos.agregar

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.saveit.model.Movimiento
import com.example.saveit.viewmodel.MovimientoViewModel
import com.example.saveit.databinding.AgregarMovimientosFragmentBinding
import java.util.*
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.ui.main.MainFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class AgregarMovimientosFragment: Fragment() {
    private var _binding: AgregarMovimientosFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mMovimientoViewModel: MovimientoViewModel
    private var tipoMovimiento by Delegates.notNull<Int>()

    val datePicker = MaterialDatePicker.Builder.datePicker()
       .setTitleText("Fecha de Movimiento")
       .build()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = AgregarMovimientosFragmentBinding.inflate(inflater, container, false)

        val itemInicial = listOf<String>("Sin Medio Pago")
        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
        (binding.medioPago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

        val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemInicial)
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

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

        binding.botonAceptar.setOnClickListener {
            tipoMovimiento = TipoMovimiento.INGRESO.valor
            insertDataToDataBase()
        }

        binding.botonCancelar.setOnClickListener {
            tipoMovimiento = TipoMovimiento.EGRESO.valor
            insertDataToDataBase()
        }
       // binding.medioPago.setOnClickListener {
         //   insertDataToDataBase()
        //}

        return binding.root
    }

    private fun insertDataToDataBase() {
//        if (inputCheck(firstName, lastName, age)) {
            val movimiento = Movimiento(0,
                binding.monto.text.toString().toDouble(),
                (Moneda.values().filter { m -> m.descripcion.equals(binding.moneda.editText?.text.toString())}).get(0).valor,
                (MedioPago.values().filter { mp -> mp.descripcion.equals(binding.medioPago.editText?.text.toString()) }).get(0).valor,
                (Categoria.values().filter { c -> c.descripcion.equals(binding.categoria.editText?.text.toString()) }).get(0).valor,
                SimpleDateFormat("dd/MM/yyyy").parse(binding.fecha.text.toString()).time,
                binding.descripcion.text.toString(),
                "Ubicacion",
                tipoMovimiento
            )

            // Add Data to Database
            mMovimientoViewModel.addMovimiento(movimiento)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
//        }
//        else {
//            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
//        }
    }

//    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
//        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker.addOnPositiveButtonClickListener {
//            var fecha: String = LocalDate.parse(datePicker.headerText, DateTimeFormatter.ofPattern("MMM dd, yyyy", SimpleDateFormat("dd/MM/yyyy"))).toString()
//            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(fecha)
            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("MMM dd, yyyy").parse(datePicker.headerText)).toString())
        }
        binding.fecha.setOnClickListener {
            onFechaMovimientoPressed()
            //datePicker.show((activity as AppCompatActivity).supportFragmentManager , "tag")
        }


//          binding.textField.setTextColor(Color.WHITE)
    }

        fun onFechaMovimientoPressed() {
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
            AgregarMovimientosFragment()
    }
}