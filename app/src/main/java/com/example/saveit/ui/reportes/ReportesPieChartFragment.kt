package com.example.saveit.ui.reportes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.saveit.data.Movimiento
import com.example.saveit.databinding.ReportesPieChartFragmentBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ReportesPieChartFragment: Fragment()  {
    private var _binding: ReportesPieChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        _binding = ReportesPieChartFragmentBinding.inflate(inflater, container, false)

        cargarGrafico()

        val categorias = arrayOf("Comida","Servicios","Alquiler")
        val adapter = ArrayAdapter(requireContext() , android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        val spinnerCategorias = binding.spinnerCategorias
        spinnerCategorias.adapter = adapter
        spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                clickEnCategoria()
            }

        }

        return binding.root
    }

    private fun clickEnCategoria(){
        Toast.makeText(requireContext(), "Hiciste click en una categoria capo", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun cargarGrafico(){
        var pieChart = binding.pieChart

        var categorias = ArrayList<PieEntry>()
        categorias.add(PieEntry(2.0f,"Servicios"))
        categorias.add(PieEntry(3.0f,"Comida"))
        categorias.add(PieEntry(5.0f,"Alquiler"))


        var pieDataSet = PieDataSet(categorias, "Categorias")
        pieDataSet.setColors(Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))


        var pieData = PieData(pieDataSet)

        pieChart.setData(pieData)

    }

}