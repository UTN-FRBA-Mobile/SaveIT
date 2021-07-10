package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.databinding.ActualDateChartFragmentBinding
import com.example.saveit.model.ResultadoReporte
import com.example.saveit.viewmodel.ReporteFechaViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActualDateChartFragment: Fragment()  {
    private var _binding: ActualDateChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: ActualDateChartFragmentArgs by navArgs()
    private lateinit var reporteFechaViewModel: ReporteFechaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ActualDateChartFragmentBinding.inflate(inflater, container, false)

        val seleccion = args.seleccion
        reporteFechaViewModel = ViewModelProvider(this).get(ReporteFechaViewModel::class.java)
        generateChartFromSelection(seleccion)

        return binding.root
    }

    private fun generateChartFromSelection(seleccion: Array<String>) {
        //ugly way of getting parameters from another fragment
        val tipoMovimiento = seleccion[0]
        val medioPagoSelec = seleccion[1]
        val categoriaSelec = seleccion[2]
        val periodoDeTiempo = PeriodosDeTiempo.getByDescripcion(seleccion[3])

        //not so ugly way of getting the data
        when (tipoMovimiento) {
            TipoMovimiento.INGRESO.descripcion -> {

                when {(categoriaSelec == "Todas") and (medioPagoSelec == "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeData(TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { ingresos ->
                        processGraph(ingresos, "Ingresos")
                    })
                }}

                when {(categoriaSelec != "Todas") and (medioPagoSelec == "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataPaymentAll(CategoriasIngreso.getByDescripcion(categoriaSelec).valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { ingresos ->
                        processGraph(ingresos, "Ingresos")
                    })
                }}

                when {(categoriaSelec == "Todas") and (medioPagoSelec != "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataCategoryAll(MedioPago.getByDescripcion(medioPagoSelec).valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { ingresos ->
                        processGraph(ingresos, "Ingresos")
                    })
                }}

                when {(categoriaSelec != "Todas") and (medioPagoSelec != "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataCategoryPayment(CategoriasIngreso.getByDescripcion(categoriaSelec).valor,
                        MedioPago.getByDescripcion(medioPagoSelec).valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { ingresos ->
                        processGraph(ingresos, "Ingresos")
                    })
                }}

            }
            TipoMovimiento.EGRESO.descripcion -> {

                when {(categoriaSelec == "Todas") and (medioPagoSelec == "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeData(TipoMovimiento.EGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { egresos ->
                        processGraph(egresos, "Egresos")
                    })
                }}

                when {(categoriaSelec != "Todas") and (medioPagoSelec == "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataPaymentAll(CategoriasIngreso.getByDescripcion(categoriaSelec).valor,
                        TipoMovimiento.EGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { egresos ->
                        processGraph(egresos, "Egresos")
                    })
                }}

                when {(categoriaSelec == "Todas") and (medioPagoSelec != "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataCategoryAll(MedioPago.getByDescripcion(medioPagoSelec).valor,
                        TipoMovimiento.EGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { egresos ->
                        processGraph(egresos, "Egresos")
                    })
                }}

                when {(categoriaSelec != "Todas") and (medioPagoSelec != "Todos") -> {
                    reporteFechaViewModel.readSpecificTimeDataCategoryPayment(CategoriasIngreso.getByDescripcion(categoriaSelec).valor,
                        MedioPago.getByDescripcion(medioPagoSelec).valor,
                        TipoMovimiento.EGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, { egresos ->
                        processGraph(egresos, "Egresos")
                    })
                }}

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun processGraph(input: List<ResultadoReporte>, inputType: String)  {
        when {
            input.isEmpty() -> {
                Toast.makeText(requireContext(),
                    "No se encontraron datos para las opciones seleccionadas",
                    Toast.LENGTH_LONG).show()
            }
            else -> {
                var pesifiedMap = mutableMapOf<Long, Float>()
                input.forEach { resultadoReporte ->
                    val day = pesifiedMap[resultadoReporte.Day]

                    val pesifiedAmount = if (resultadoReporte.Moneda == Moneda.PESO.valor) resultadoReporte.Value else resultadoReporte.Value * resultadoReporte.CotizacionDolar
                    if (day == null){
                        pesifiedMap[resultadoReporte.Day] = pesifiedAmount
                    }else{
                        val previousValue = pesifiedMap.getOrDefault(resultadoReporte.Day, 0f)
                        pesifiedMap[resultadoReporte.Day] = previousValue + pesifiedAmount
                    }
                }

                var xLabel: ArrayList<String> = ArrayList()
                pesifiedMap.forEach { pesifiedEntry ->
                    var dayDateTime = getDateTime(pesifiedEntry.key.toString())
                    xLabel.add(dayDateTime.toString())
                }

                var shiftedMap = shiftDataset(pesifiedMap)

                val lineIngresosEntry = shiftedMap.map { entry -> Entry(entry.key.toFloat(), entry.value) }
                val linedataset1 = LineDataSet(lineIngresosEntry, inputType)
                linedataset1.color = resources.getColor(R.color.green)
                linedataset1.setDrawCircles(true)
                linedataset1.setCircleColor(resources.getColor(R.color.green))
                linedataset1.circleRadius = 5f
                linedataset1.setDrawFilled(true)
                linedataset1.fillColor = resources.getColor(R.color.green)
                linedataset1.fillAlpha = 60
                linedataset1.valueTextSize = 0f

                val xAxis: XAxis = binding.lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(true)
                xAxis.isGranularityEnabled = true
                xAxis.setCenterAxisLabels(true)
                xAxis.granularity = 1f
                xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)

                val data = LineData(linedataset1)
                binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
                binding.lineChart.data = data
                binding.lineChart.animateXY(1500, 1500)
                binding.lineChart.description.text = ""
            }
        }
    }

    private fun shiftDataset(originalDataSet: Map<Long, Float>): Map<Long, Float> {
        return when {
            originalDataSet.isNotEmpty() -> {
                var mutableList = listOf<Pair<Long, Float>>().toMutableList()

                var i = 0
                originalDataSet.forEach { originalEntry ->
                    val tempEntry = Pair(i.toLong(), originalEntry.value)
                    mutableList.add(tempEntry)
                    i++
                }

                return mutableList.toMap()
            }
            else -> {
                originalDataSet
            }
        }
    }

    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
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
