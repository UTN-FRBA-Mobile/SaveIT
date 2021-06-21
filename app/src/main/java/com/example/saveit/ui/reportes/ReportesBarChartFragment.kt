package com.example.saveit.ui.reportes

import android.graphics.Color
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
import androidx.lifecycle.ViewModelProvider
import com.example.saveit.R
import com.example.saveit.data.CategoriasGasto
import com.example.saveit.data.Meses
import com.example.saveit.databinding.ReportesBarChartFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.ui.movimientos.lista.ListaMovimientosAdapter
import com.example.saveit.viewmodel.MovimientoViewModel
import com.github.mikephil.charting.data.*
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer


class ReportesBarChartFragment : Fragment() {
    private var _binding: ReportesBarChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMovimientoViewModel: MovimientoViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesBarChartFragmentBinding.inflate(inflater, container, false)

        val adapter = ListaMovimientosAdapter()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)
        mMovimientoViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            if (user.size == 0)
                Toast.makeText(requireContext(), "No hay movimientos cargados", Toast.LENGTH_LONG).show()

            adapter.setData(user)
        })
        loadYearsAndMonths()

        binding.botonGenerarReporte.setOnClickListener {
            val anio = binding.seleccionAnioBarChart.findViewById<TextInputLayout>(R.id.seleccion_anio_barChart).editText!!.text.toString()
            val mes = binding.seleccionMesBarChart.findViewById<TextInputLayout>(R.id.seleccion_mes_barChart).editText!!.text.toString()

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

                mMovimientoViewModel.readAllDataBetween(desde, hasta).observe(viewLifecycleOwner, Observer { user ->
                    if (user.size == 0){
                        Toast.makeText(requireContext(), "No hay movimientos cargados", Toast.LENGTH_LONG).show()
                    }
                    cargarGrafico(user )
                })
            }
            else {
                Toast.makeText(requireContext(), "Debe seleccionar un año", Toast.LENGTH_LONG).show()
            }
        }

        binding.botonLimpiarFormulario.setOnClickListener {
            cleanForm()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun cargarGrafico(){

        var barChart = binding.barChart

        var categorias1 = ArrayList<BarEntry>()
        categorias1.add(BarEntry(1f,100f))
        var barDataSet1 = BarDataSet(categorias1, "Delivery")
        barDataSet1.setColors(Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))

        var categorias2 = ArrayList<BarEntry>()
        categorias2.add(BarEntry(2f,200f))
        var barDataSet2 = BarDataSet(categorias2, "Servicios")
        barDataSet2.setColors(Color.rgb(255, 255, 0))


        var categorias3 = ArrayList<BarEntry>()
        categorias3.add(BarEntry(3f,300f))
        var barDataSet3 = BarDataSet(categorias3, "Diversion")
        barDataSet3.setColors(Color.rgb(106, 150, 31))

        var barData = BarData()
        barData.addDataSet(barDataSet1)

        barChart.setData(barData)
        barChart.animateXY(1500, 1500)

        binding.barChart.notifyDataSetChanged()
        binding.barChart.invalidate()

    }

    private fun cleanForm() {
        (binding.seleccionAnioBarChart.editText as? AutoCompleteTextView)?.setText("")
        (binding.seleccionMesBarChart.editText as? AutoCompleteTextView)?.setText("")
    }


    private fun cargarGrafico(movimientos : List<Movimiento> ) {
        var barChart = binding.barChart

        val categorias = CategoriasGasto.values()
        var cero = 0
        var total_x_categoria =  FloatArray(CategoriasGasto.values().size)

        for(movimiento in movimientos){
            total_x_categoria[movimiento.categoria-1] = (total_x_categoria[movimiento.categoria-1] + movimiento.monto).toFloat()
        }

        var barData = BarData()

        val colores = arrayOf(
            Color.rgb(23, 83, 99),
            Color.rgb(23, 45, 99),
            Color.rgb(77, 23, 99),
            Color.rgb(99, 23, 45),
            Color.rgb(45, 99, 23),
            Color.rgb(196, 76, 48),
            Color.rgb(150, 48, 196),
            Color.rgb(196, 113, 48),
            Color.rgb(168, 196, 48))


        for(i in 0..categorias.size-1) {
            if(total_x_categoria[i] > 0){
                var categorias = ArrayList<BarEntry>()
                categorias.add(BarEntry(i.toFloat(),total_x_categoria[i]))
                var barDataSet = BarDataSet(categorias, CategoriasGasto.getByValor(i+1))
                barDataSet.setColors(colores[i])
                barData.addDataSet(barDataSet)
            }
        }

        //barData.addDataSet()
        barChart.setData(barData)
        barChart.animateXY(1500, 1500)

        binding.barChart.notifyDataSetChanged()
        binding.barChart.invalidate()

    }



    fun getFirstDayOfMonth(year: Int, month: Int): Long {
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

    fun getLastDayOfMonth(year: Int, month: Int): Long {
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

    fun formatDate(fecha: Long): String {
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }


    fun loadYearsAndMonths() {
        val asc = Array(10) { i -> (Calendar.getInstance().get(Calendar.YEAR)- i).toString() }
        val itemsSeleccionAnios = asc.map { it }
        val adapterSeleccionAnios = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionAnios)
        (binding.seleccionAnioBarChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMesBarChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)
    }

}