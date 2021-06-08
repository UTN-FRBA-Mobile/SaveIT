package com.example.saveit.ui.reportes

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.saveit.R
import com.example.saveit.data.Meses
import com.example.saveit.databinding.ReportesBarChartFragmentBinding
import com.github.mikephil.charting.data.*


class ReportesBarChartFragment : Fragment() {
    private var _binding: ReportesBarChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesBarChartFragmentBinding.inflate(inflater, container, false)

        val asc = Array(10) { i -> (Calendar.getInstance().get(Calendar.YEAR)- i).toString() }
        val itemsSeleccionAnios = asc.map { it }
        val adapterSeleccionAnios = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionAnios)
        (binding.seleccionAnio.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMes.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)

        cargarGrafico()
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
        barDataSet1.setColors(Color.rgb(193, 37, 82))

        var categorias2 = ArrayList<BarEntry>()
        categorias2.add(BarEntry(2f,200f))
        var barDataSet2 = BarDataSet(categorias2, "Servicios")
        barDataSet2.setColors(Color.rgb(255, 255, 0))


        var categorias3 = ArrayList<BarEntry>()
        categorias3.add(BarEntry(3f,300f))
        var barDataSet3 = BarDataSet(categorias3, "Diversion")
        barDataSet3.setColors(Color.rgb(106, 150, 31))



        var barData = BarData(barDataSet1,barDataSet2,barDataSet3)

        barChart.setData(barData)
        barChart.animate()




    }
}