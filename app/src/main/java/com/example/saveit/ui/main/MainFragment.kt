package com.example.saveit.ui.main

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.saveit.R
import com.example.saveit.data.Moneda
import com.example.saveit.data.TipoMovimiento
import com.example.saveit.databinding.MainFragmentBinding
import com.example.saveit.viewmodel.MovimientoViewModel
import kotlinx.android.synthetic.main.reportes_date_chart_fragment.*
import java.util.*
import kotlin.collections.HashMap

private const val ARG_TITLE = "title"
class MainFragment: Fragment() {
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var title: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var monthsDictionary: HashMap<Int, String> = HashMap()

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
        }

        monthsDictionary.put(0, "Enero")
        monthsDictionary.put(1, "Febrero")
        monthsDictionary.put(2, "Marzo")
        monthsDictionary.put(3, "Abril")
        monthsDictionary.put(4, "Mayo")
        monthsDictionary.put(5, "Junio")
        monthsDictionary.put(6, "Julio")
        monthsDictionary.put(7, "Agosto")
        monthsDictionary.put(8, "Septiembre")
        monthsDictionary.put(9, "Octubre")
        monthsDictionary.put(10, "Noviembre")
        monthsDictionary.put(11, "Diciembre")
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

        binding.ahorroButton.setOnClickListener {
            onButtonAhorroPressed()
        }

        binding.reportesButton.setOnClickListener {
            onButtonReportesPressed()
        }

        binding.listaMovimientosButton.setOnClickListener {
            onButtonlistaMovimientosPressed()
        }

        binding.agregarMovimientosButton.setOnClickListener {
            onButtonagregarMovimientosPressed()
        }
    }

    fun setSummaryValues() {
        val cal = Calendar.getInstance()

        binding.textViewTituloBalance.text = monthsDictionary.get(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR).toString()

        var ingresosDisplay: Double = 0.toDouble()
        var gastosDisplay: Double = 0.toDouble()
        var ahorrosDisplay: Double

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        var desde = getFirstDayOfMonth()
        var hasta = getLastDayOfMonth()

        mMovimientoViewModel.readIngresos(desde, hasta).observe(viewLifecycleOwner, { ingresos ->
            if (ingresos != null)
                ingresosDisplay = String.format("%.2f", ingresos).toDouble()

            binding.textViewMontoIngresos.text = "+ \$" + ingresosDisplay

            mMovimientoViewModel.readGastos(desde, hasta).observe(viewLifecycleOwner, { gastos ->
                if (gastos != null)
                    gastosDisplay = String.format("%.2f", gastos).toDouble()

                binding.textViewMontoGastos.text = "- \$" + gastosDisplay

                val saldo = ingresosDisplay - gastosDisplay

                if (saldo >= 0)
                    binding.textViewMontoTotal.text = "+ \$" + saldo
                else
                    binding.textViewMontoTotal.text = "- \$" + saldo * -1

                mMovimientoViewModel.readAhorros().observe(viewLifecycleOwner, { ahorros ->
                    var actualSavings = 0.0
                    if (ahorros != null) {
                         actualSavings = ahorros.map{ ahorro ->
                            if (ahorro.moneda == Moneda.PESO.valor
                                && ahorro.tipoMovimiento == TipoMovimiento.INGRESO.valor) ahorro.monto * -1.0

                            else if (ahorro.moneda == Moneda.PESO.valor
                                && ahorro.tipoMovimiento == TipoMovimiento.EGRESO.valor) ahorro.monto

                            else if (ahorro.moneda == Moneda.DOLAR.valor
                                && ahorro.tipoMovimiento == TipoMovimiento.INGRESO.valor) ahorro.monto * ahorro.cotizacionDolar * -1.0

                            else if (ahorro.moneda == Moneda.DOLAR.valor
                                && ahorro.tipoMovimiento == TipoMovimiento.EGRESO.valor) ahorro.monto * ahorro.cotizacionDolar

                            else 0.0}.sum()
                    }
                    ahorrosDisplay = String.format("%.2f", actualSavings).toDouble()
                    binding.textViewMontoAhorros.text = "+ \$" + ahorrosDisplay
                })
            })
        })
    }

    fun getFirstDayOfMonth(): Long {
        // get today and clear time of day
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        // get start of the month
        cal[Calendar.DAY_OF_MONTH] = 1

        val fecha = formatDate(cal.timeInMillis)

        return SimpleDateFormat("dd/MM/yyyy").parse(fecha).time
    }

    fun getLastDayOfMonth(): Long {
        // get today and clear time of day
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        // get start of the month
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val fecha = formatDate(cal.timeInMillis)

        return SimpleDateFormat("dd/MM/yyyy").parse(fecha).time
    }

    fun formatDate(fecha: Long): String {
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
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