    package com.example.saveit.ui.movimientos.agregar

import android.content.Context
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
import com.example.saveit.data.User
import com.example.saveit.data.UserViewModel
import com.example.saveit.databinding.AgregarMovimientosFragmentBinding
import com.example.saveit.R
import com.google.android.material.datepicker.MaterialDatePicker

    class AgregarMovimientosFragment: Fragment() {
    private var _binding: AgregarMovimientosFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
   // private var listener: OnFragmentInteractionListener? = null
   val datePicker = MaterialDatePicker.Builder.datePicker()
       .setTitleText("Fecha de Movimiento")
       .build()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = AgregarMovimientosFragmentBinding.inflate(inflater, container, false)

        val itemsMedioPago = listOf("Tarjeta Debito", "Tarjeta Credito", "Efectivo", "QR", "Billetera Virtual")
        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMedioPago)
        (binding.medioPago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

        val itemsCategorias = listOf("Comida", "Viajes", "Electronica", "Vestimenta")
        val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemsCategorias)
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

        val itemsMonedas = listOf("u\$s", "$")
        val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMonedas)
        (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)

       // binding.medioPago.setOnClickListener {
         //   insertDataToDataBase()
        //}

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker.addOnPositiveButtonClickListener {
            (binding.fechaMovimiento.editText as? AutoCompleteTextView)?.setText(datePicker.headerText)
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
   // override fun onAttach(context: Context) {
    //    super.onAttach(context)
      //  if (context is OnFragmentInteractionListener) {
       //     listener = context
        //} else {
        //    throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        //}
   // }

    //override fun onDetach() {
      //  super.onDetach()
       // listener = null
    //}


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
   // interface OnFragmentInteractionListener {
     //   fun showFragment(fragment: Fragment)
    //}

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