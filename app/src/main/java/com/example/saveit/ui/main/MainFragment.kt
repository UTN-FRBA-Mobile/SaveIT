package com.example.saveit.ui.main

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.saveit.R
import com.example.saveit.NavegacionInterface
//import androidx.lifecycle.ViewModelProvider
import com.example.saveit.databinding.MainFragmentBinding
import com.example.saveit.ui.ahorro.AhorroFragment
import com.example.saveit.ui.movimientos.agregar.AgregarMovimientosFragment
import com.example.saveit.ui.movimientos.agregar.AgregarUsuarioFragment
import com.example.saveit.ui.movimientos.lista.ListaMovimientosAdapter
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragment
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragmentDirections
import com.example.saveit.ui.reportes.ReportesFragment
import com.example.saveit.viewmodel.MovimientoViewModel
import kotlinx.android.synthetic.main.custom_movimiento_row.view.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters

private const val ARG_TITLE = "title"
class MainFragment: Fragment() {
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var title: String? = null
    private var listener: OnFragmentInteractionListener? = null
   // private lateinit var viewModel: MainViewModel

    private var monthsDictionary: HashMap<Int, String> = HashMap()

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
        }

        monthsDictionary.put(0, "Enero");
        monthsDictionary.put(1, "Febrero");
        monthsDictionary.put(2, "Marzo");
        monthsDictionary.put(3, "Abril");
        monthsDictionary.put(4, "Mayo");
        monthsDictionary.put(5, "Junio");
        monthsDictionary.put(6, "Julio");
        monthsDictionary.put(7, "Agosto");
        monthsDictionary.put(8, "Septiembre");
        monthsDictionary.put(9, "Octubre");
        monthsDictionary.put(10, "Noviembre");
        monthsDictionary.put(11, "Diciembre");
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSummaryValues()

        //binding.ahorroButton.text = "Ahorro"
        binding.ahorroButton.setOnClickListener {
            onButtonAhorroPressed()
        }
        //binding.reportesButton.text = "Reportes"
        binding.reportesButton.setOnClickListener {
            onButtonReportesPressed()
        }
        //binding.listaMovimientosButton.text = "Lista Movimientos"
        binding.listaMovimientosButton.setOnClickListener {
            onButtonlistaMovimientosPressed()
        }
        //binding.agregarMovimientosButton.text = "Agregar Movimientos"
        binding.agregarMovimientosButton.setOnClickListener {
            onButtonagregarMovimientosPressed()
        }
    }

    fun setSummaryValues() {
        binding.balanceMesTextView.text = monthsDictionary.get(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR).toString()

        var ingresosDisplay: Double = 0.toDouble()
        var gastosDisplay: Double = 0.toDouble()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        var desde = SimpleDateFormat("dd/MM/yyyy").parse("01/06/2021").time
        var hasta = SimpleDateFormat("dd/MM/yyyy").parse("30/06/2021").time
        var a = TemporalAdjusters.firstDayOfMonth()
        var b = TemporalAdjusters.lastDayOfMonth()
        mMovimientoViewModel.readIngresos(desde, hasta).observe(viewLifecycleOwner, Observer { ingresos ->
            if (ingresos != null)
                ingresosDisplay = ingresos

            binding.ingresosTextView.text = "Ingresos: + \$" + ingresosDisplay
        })

        mMovimientoViewModel.readGastos().observe(viewLifecycleOwner, Observer { gastos ->
            if (gastos != null)
                gastosDisplay = gastos

            binding.gastosTextView.text = "Gastos:   -  \$" + gastosDisplay

            val saldo = ingresosDisplay - gastosDisplay

            if (saldo >= 0)
                binding.saldoTextView.text = "Saldo:      + \$" + saldo
            else
                binding.saldoTextView.text = "Saldo:      - \$" + saldo
        })
    }

    fun onButtonAhorroPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_ahorroFragment)
    }
    fun onButtonReportesPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_reportesFragment)
    }
    fun onButtonlistaMovimientosPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_listaMovimientosFragment2)
    }
    fun onButtonagregarMovimientosPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_agregarMovimientosFragment)
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
         * @param title Title.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance(title: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }
}