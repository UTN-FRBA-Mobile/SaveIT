package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.saveit.R
import com.example.saveit.databinding.ActualDateChartFragmentBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ActualDateChartFragment: Fragment()  {
    private var _binding: ActualDateChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ActualDateChartFragmentBinding.inflate(inflater, container, false)

        var seleccion = arguments?.getStringArrayList("seleccion")

        generateChartFromSelection(seleccion)

        return binding.root
    }

    private fun generateChartFromSelection(seleccion: ArrayList<String>?) {
        //ugly way of getting parameters from another fragment
        val medioPagoSelec = seleccion?.get(0)
        val categoriaSelec = seleccion?.get(1)
        val monedaSelec = seleccion?.get(2)
        val periodoSelec = seleccion?.get(3)
        val lineEgresosEntry = ArrayList<Entry>()
        val lineIngresosEntry = ArrayList<Entry>()

        //this should be populated using a parameterized database query

        //FIRST ->
        //filter the results by the timeframe (one week, one month, three months, six months, one year)
        //filter the results by currency (either USD or ARS)

        //THEN ->
        //summarize the results (sum of ammount) by Type (ingress or egress) and an appropriate timeframe
        //(i.e. week -> summarize every 1 day -> 7 datapoints, month -> summarize every 2 or 4 days... etc)

        //RESULTS ->
        //this query should return a list of records with three columns
        //data point timestamp, ingress ammount, egress ammount

        //PSEUDO CODE
        //SELECT ingresos_totales = SUMIF(TIPO == "INGRESO", MONTO), egresos_totales = SUMIF(TIPO == "EGRESO", MONTO)
        //WHERE CURRENCY = "ARS" and TIMESTAMP >= today()-7DAYS
        //FROM movimientostable
        //GROUP BY BIN(DATE, 1 DAY)

        //examples for UI mock
        when {
            medioPagoSelec.equals("Todos")
                    and categoriaSelec.equals("Todos")
                    and monedaSelec.equals("$")
                    and periodoSelec.equals("Semana") -> {

                lineIngresosEntry.add(Entry(0f, 15000f))
                lineIngresosEntry.add(Entry(4f, 10000f))

                lineEgresosEntry.add(Entry(0f, 1000f))
                lineEgresosEntry.add(Entry(1f, 2000f))
                lineEgresosEntry.add(Entry(2f, 3000f))
                lineEgresosEntry.add(Entry(3f, 4000f))
                lineEgresosEntry.add(Entry(4f, 5000f))
                lineEgresosEntry.add(Entry(5f, 6000f))
                lineEgresosEntry.add(Entry(6f, 5000f))

                val linedataset1 = LineDataSet(lineIngresosEntry, "Ingresos")
                linedataset1.color = resources.getColor(R.color.green)
                linedataset1.setDrawCircles(true)
                linedataset1.setCircleColor(resources.getColor(R.color.green))
                linedataset1.circleRadius = 5f
                linedataset1.setDrawFilled(true)
                linedataset1.fillColor = resources.getColor(R.color.green)
                linedataset1.fillAlpha = 60

                val linedataset2 = LineDataSet(lineEgresosEntry, "Egresos")
                linedataset2.color = resources.getColor(R.color.red)
                linedataset2.setDrawCircles(true)
                linedataset2.setCircleColor(resources.getColor(R.color.red))
                linedataset2.circleRadius = 5f
                linedataset2.setDrawFilled(true)
                linedataset2.fillColor = resources.getColor(R.color.red)
                linedataset2.fillAlpha = 60

                val data = LineData(linedataset1, linedataset2)

                binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
                binding.lineChart.data = data
                binding.lineChart.animateXY(3000, 3000)
            }

            medioPagoSelec.equals("Todos")
                    and categoriaSelec.equals("Todos")
                    and monedaSelec.equals("$")
                    and periodoSelec.equals("Mes") -> {

                lineIngresosEntry.add(Entry(0f, 15000f))
                lineIngresosEntry.add(Entry(4f, 10000f))

                lineEgresosEntry.add(Entry(0f, 1000f))
                lineEgresosEntry.add(Entry(1f, 2000f))
                lineEgresosEntry.add(Entry(2f, 3000f))
                lineEgresosEntry.add(Entry(3f, 4000f))
                lineEgresosEntry.add(Entry(4f, 5000f))
                lineEgresosEntry.add(Entry(5f, 6000f))
                lineEgresosEntry.add(Entry(6f, 5000f))

                lineIngresosEntry.add(Entry(7f, 15000f))
                lineIngresosEntry.add(Entry(11f, 10000f))

                lineEgresosEntry.add(Entry(7f, 1000f))
                lineEgresosEntry.add(Entry(8f, 2000f))
                lineEgresosEntry.add(Entry(9f, 3000f))
                lineEgresosEntry.add(Entry(10f, 4000f))
                lineEgresosEntry.add(Entry(11f, 5000f))
                lineEgresosEntry.add(Entry(12f, 6000f))
                lineEgresosEntry.add(Entry(13f, 5000f))

                lineIngresosEntry.add(Entry(14f, 15000f))
                lineIngresosEntry.add(Entry(18f, 10000f))

                lineEgresosEntry.add(Entry(14f, 1000f))
                lineEgresosEntry.add(Entry(15f, 2000f))
                lineEgresosEntry.add(Entry(16f, 3000f))
                lineEgresosEntry.add(Entry(17f, 4000f))
                lineEgresosEntry.add(Entry(18f, 5000f))
                lineEgresosEntry.add(Entry(19f, 6000f))
                lineEgresosEntry.add(Entry(20f, 5000f))

                lineIngresosEntry.add(Entry(21f, 15000f))
                lineIngresosEntry.add(Entry(25f, 10000f))

                lineEgresosEntry.add(Entry(21f, 1000f))
                lineEgresosEntry.add(Entry(22f, 2000f))
                lineEgresosEntry.add(Entry(23f, 3000f))
                lineEgresosEntry.add(Entry(24f, 4000f))
                lineEgresosEntry.add(Entry(25f, 5000f))
                lineEgresosEntry.add(Entry(26f, 6000f))
                lineEgresosEntry.add(Entry(27f, 5000f))

                val linedataset1 = LineDataSet(lineIngresosEntry, "Ingresos")
                linedataset1.color = resources.getColor(R.color.green)
                linedataset1.setDrawCircles(true)
                linedataset1.setCircleColor(resources.getColor(R.color.green))
                linedataset1.circleRadius = 5f
                linedataset1.setDrawFilled(true)
                linedataset1.fillColor = resources.getColor(R.color.green)
                linedataset1.fillAlpha = 60

                val linedataset2 = LineDataSet(lineEgresosEntry, "Egresos")
                linedataset2.color = resources.getColor(R.color.red)
                linedataset2.setDrawCircles(true)
                linedataset2.setCircleColor(resources.getColor(R.color.red))
                linedataset2.circleRadius = 5f
                linedataset2.setDrawFilled(true)
                linedataset2.fillColor = resources.getColor(R.color.red)
                linedataset2.fillAlpha = 60

                val data = LineData(linedataset1, linedataset2)

                binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
                binding.lineChart.data = data
                binding.lineChart.animateXY(3000, 3000)
            }
            else -> {
                Toast.makeText(requireContext(),
                    "No se encontraron datos para las opciones seleccionadas",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        @JvmStatic
        fun newInstance(seleccion: ArrayList<String>) = ActualDateChartFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList("seleccion", seleccion)
            }
        }
    }

}
