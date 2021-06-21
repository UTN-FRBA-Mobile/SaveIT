package com.example.saveit.ui.ahorro

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saveit.R
import com.example.saveit.data.Meses
import com.example.saveit.databinding.AhorroFragmentBinding
import com.example.saveit.viewmodel.MovimientoViewModel
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AhorroFragment: Fragment() {
    private var _binding: AhorroFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    private var listener: AhorroFragment.OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = AhorroFragmentBinding.inflate(inflater, container, false)

        // RecyclerView
        val adapter = AhorroAdapter()
        val recyclerView = binding.recyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)
        mMovimientoViewModel.readAhorros().observe(viewLifecycleOwner, Observer { user ->
            if (user.isEmpty())
                Toast.makeText(requireContext(), "No hay ahorros cargados", Toast.LENGTH_LONG).show()

            adapter.setData(user)

            val monto = adapter.getMontoTotal()

            if (monto >= 0)
                binding.montoTotal.text = "Total: + \$" + monto
            else
                binding.montoTotal.text = "Total: - \$" + monto * -1
        })

        loadYearsAndMonths()

        binding.botonBuscar.setOnClickListener {
            val anio = binding.seleccionAnio.findViewById<TextInputLayout>(R.id.seleccion_anio).editText!!.text.toString()
            val mes = binding.seleccionMes.findViewById<TextInputLayout>(R.id.seleccion_mes).editText!!.text.toString()

            if (anio != "") {
                var desde: Long
                var hasta: Long

                if (mes != "") {
                    desde = getFirstDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion(mes))
                    hasta = getLastDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion(mes))
                }
                else {
                    desde = getFirstDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion("Enero"))
                    hasta = getLastDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion("Diciembre"))
                }

                mMovimientoViewModel.readAhorrosBetween(desde, hasta).observe(viewLifecycleOwner, Observer { user ->
                    if (user.size == 0)
                        Toast.makeText(requireContext(), "No hay ahorros cargados", Toast.LENGTH_LONG).show()

                    adapter.setData(user)

                    val monto = adapter.getMontoTotal()

                    if (monto >= 0)
                        binding.montoTotal.text = "Total: + \$" + monto
                    else
                        binding.montoTotal.text = "Total: - \$" + monto * -1
                })
            }
            else {
                Toast.makeText(requireContext(), "Debe seleccionar un año", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun loadYearsAndMonths() {
        val asc = Array(10) { i -> (Calendar.getInstance().get(Calendar.YEAR)- i).toString() }
        val itemsSeleccionAnios = asc.map { it }
        val adapterSeleccionAnios = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionAnios)
        (binding.seleccionAnio.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMes.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)
    }

    private fun getFirstDayOfMonth(year: Int, month: Int): Long {
        // get today and clear time of day
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        // get start of the month
        cal[Calendar.DAY_OF_MONTH] = 1

        val fecha = formatDate(cal.timeInMillis)

        return SimpleDateFormat("dd/MM/yyyy").parse(fecha).time
    }

    private fun getLastDayOfMonth(year: Int, month: Int): Long {
        // get today and clear time of day
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        // get start of the month
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val fecha = formatDate(cal.timeInMillis)

        return SimpleDateFormat("dd/MM/yyyy").parse(fecha).time
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
        else {
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
            AhorroFragment()
    }
}