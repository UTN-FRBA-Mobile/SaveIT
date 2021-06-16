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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.saveit.R
import com.example.saveit.data.CategoriasGasto
import com.example.saveit.data.CategoriasIngreso
import com.example.saveit.data.Meses
import com.example.saveit.databinding.ReportesPieChartFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.ui.movimientos.lista.ListaMovimientosAdapter
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragment
import com.example.saveit.viewmodel.MovimientoViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class ReportesPieChartFragment: Fragment()  {
    private var _binding: ReportesPieChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMovimientoViewModel: MovimientoViewModel
    private var listener: ListaMovimientosFragment.OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesPieChartFragmentBinding.inflate(inflater, container, false)

        val adapter = ListaMovimientosAdapter()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)
        mMovimientoViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            if (user.size == 0)
                Toast.makeText(requireContext(), "No hay movimientos cargados", Toast.LENGTH_LONG).show()

            adapter.setData(user)
        })

        binding.botonGenerarReporte.setOnClickListener {
            cargarGrafico()
        }

        loadYearsAndMonths()
        //cargarGrafico()

        binding.botonGenerarReporte.setOnClickListener {
            val anio = binding.seleccionAnioPieChart.findViewById<TextInputLayout>(R.id.seleccion_anio_pieChart).editText!!.text.toString()
            val mes = binding.seleccionMesPieChart.findViewById<TextInputLayout>(R.id.seleccion_mes_pieChart).editText!!.text.toString()

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

    private fun cleanForm() {
        (binding.seleccionMesPieChart.editText as? AutoCompleteTextView)?.setText("")
        (binding.seleccionAnioPieChart.editText as? AutoCompleteTextView)?.setText("")
    }


    private fun cargarGrafico(movimientos : List<Movimiento> ) {
        var pieChart = binding.pieChart


        val categorias = CategoriasGasto.values()
        var cero = 0
        var total_x_categoria =  FloatArray(CategoriasGasto.values().size)

        for(movimiento in movimientos){
            total_x_categoria[movimiento.categoria-1] = (total_x_categoria[movimiento.categoria-1] + movimiento.monto).toFloat()
        }

        var categoriasPieEntry = ArrayList<PieEntry>()
        for(i in 0..categorias.size-1) {
            if(total_x_categoria[i] > 0){
                categoriasPieEntry.add(PieEntry(total_x_categoria[i],CategoriasGasto.getByValor(i+1)))
            }

        }

        var pieDataSet = PieDataSet(categoriasPieEntry, "Categorias")
        pieDataSet.setColors(
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))

        pieDataSet.setValueTextSize(16F)

        pieDataSet.setValueTextColors(listOf(Color.BLACK))

        var pieData = PieData(pieDataSet)

        pieChart.setData(pieData)
        pieChart.setCenterText("Categorias")
        pieChart.animate()

    }

    private fun cargarGrafico(){
        var pieChart = binding.pieChart

        var categorias = ArrayList<PieEntry>()

        categorias.add(PieEntry(2.0f,"Servicios"))
        categorias.add(PieEntry(3.0f,"Comida"))
        categorias.add(PieEntry(5.0f,"Alquiler"))

        var pieDataSet = PieDataSet(categorias, "Categorias")
        pieDataSet.setColors(
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))

        pieDataSet.setValueTextSize(16F)

        pieDataSet.setValueTextColors(listOf(Color.BLACK))

        var pieData = PieData(pieDataSet)

        pieChart.setData(pieData)
        pieChart.setCenterText("Categorias")
        pieChart.animate()

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
        (binding.seleccionAnioPieChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMesPieChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)
    }


}