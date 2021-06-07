package com.example.saveit.ui.reportes

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.saveit.NavegacionInterface
import com.example.saveit.R
import com.example.saveit.data.Meses
import com.example.saveit.data.PeriodosDeTiempo
import com.example.saveit.databinding.ReportesPieChartFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ReportesPieChartFragment: Fragment()  {
    private var _binding: ReportesPieChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesPieChartFragmentBinding.inflate(inflater, container, false)

        cargarGrafico()


        val asc = Array(10) { i -> (Calendar.getInstance().get(Calendar.YEAR)- i).toString() }
        val itemsSeleccionAnios = asc.map { it }
        val adapterSeleccionAnios = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionAnios)
        (binding.seleccionAnio.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMes.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)

        binding.botonGenerarReporte.setOnClickListener {
            cargarGrafico()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

}