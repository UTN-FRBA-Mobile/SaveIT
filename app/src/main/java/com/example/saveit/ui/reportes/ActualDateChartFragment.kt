package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.databinding.ActualDateChartFragmentBinding
import com.example.saveit.model.ResultadoReporte
import com.example.saveit.viewmodel.ReporteFechaViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

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
        val medioPagoSelec = seleccion[0]
        val categoriaSelec = seleccion[1]
        val monedaSelec = seleccion[2]
        val periodoSelec = seleccion[3]
        val moneda = Moneda.getByDescripcion(monedaSelec)
        val periodoDeTiempo = PeriodosDeTiempo.getByDescripcion(periodoSelec)

        //even uglier way of getting the data
        when {
            categoriaSelec.equals("Todos")
            and medioPagoSelec.equals("Todos") -> {
                reporteFechaViewModel.readSpecificTimeData(moneda.valor,
                    TipoMovimiento.EGRESO.valor,
                    periodoDeTiempo.query_str).observe(viewLifecycleOwner, { egresos ->

                    reporteFechaViewModel.readSpecificTimeData(moneda.valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { ingresos ->

                        when {
                            egresos.isEmpty() and ingresos.isEmpty() -> {
                                Toast.makeText(requireContext(),
                                    "No se encontraron datos para las opciones seleccionadas",
                                    Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                val lineEgresosEntry = egresos.map { egreso -> Entry(egreso.Day.toFloat(), egreso.Value) }
                                val lineIngresosEntry = ingresos.map { ingreso -> Entry(ingreso.Day.toFloat(), ingreso.Value) }
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
                                binding.lineChart.animateXY(1500, 1500)
                            }
                        }
                    })
                })
            }

            categoriaSelec.equals("Todos")
            and !medioPagoSelec.equals("Todos") -> {
                var medioDePago = MedioPago.getByDescripcion(medioPagoSelec)
                reporteFechaViewModel.readSpecificTimeDataCategoryAll(moneda.valor,
                    medioDePago.valor,
                    TipoMovimiento.EGRESO.valor,
                    periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { egresos ->

                    reporteFechaViewModel.readSpecificTimeDataCategoryAll(moneda.valor,
                        medioDePago.valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { ingresos ->

                        when {
                            egresos.isEmpty() and ingresos.isEmpty() -> {
                                Toast.makeText(requireContext(),
                                    "No se encontraron datos para las opciones seleccionadas",
                                    Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                var lineEgresosEntry = egresos.map { egreso -> Entry(egreso.Day.toFloat(), egreso.Value) }
                                var lineIngresosEntry = ingresos.map { ingreso -> Entry(ingreso.Day.toFloat(), ingreso.Value) }
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
                                binding.lineChart.animateXY(1500, 1500)
                            }
                        }
                    })
                })
            }

            !categoriaSelec.equals("Todos")
            and medioPagoSelec.equals("Todos") -> {
                var categoria = Categoria.getByDescripcion(categoriaSelec)
                reporteFechaViewModel.readSpecificTimeDataPaymentAll(moneda.valor,
                    categoria.valor,
                    TipoMovimiento.EGRESO.valor,
                    periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { egresos ->

                    reporteFechaViewModel.readSpecificTimeDataPaymentAll(moneda.valor,
                        categoria.valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { ingresos ->

                        when {
                            egresos.isEmpty() and ingresos.isEmpty() -> {
                                Toast.makeText(requireContext(),
                                    "No se encontraron datos para las opciones seleccionadas",
                                    Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                var lineEgresosEntry = egresos.map { egreso -> Entry(egreso.Day.toFloat(), egreso.Value) }
                                var lineIngresosEntry = ingresos.map { ingreso -> Entry(ingreso.Day.toFloat(), ingreso.Value) }
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
                                binding.lineChart.animateXY(1500, 1500)
                            }
                        }
                    })
                })
            }

            else -> {
                var categoria = Categoria.getByDescripcion(categoriaSelec)
                var medioDePago = MedioPago.getByDescripcion(medioPagoSelec)
                reporteFechaViewModel.readSpecificTimeDataCategoryPayment(moneda.valor,
                    categoria.valor,
                    medioDePago.valor,
                    TipoMovimiento.EGRESO.valor,
                    periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { egresos ->

                    reporteFechaViewModel.readSpecificTimeDataCategoryPayment(moneda.valor,
                        categoria.valor,
                        medioDePago.valor,
                        TipoMovimiento.INGRESO.valor,
                        periodoDeTiempo.query_str).observe(viewLifecycleOwner, Observer { ingresos ->

                        when {
                            egresos.isEmpty() and ingresos.isEmpty() -> {
                                Toast.makeText(requireContext(),
                                    "No se encontraron datos para las opciones seleccionadas",
                                    Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                var lineEgresosEntry = egresos.map { egreso -> Entry(egreso.Day.toFloat(), egreso.Value) }
                                var lineIngresosEntry = ingresos.map { ingreso -> Entry(ingreso.Day.toFloat(), ingreso.Value) }
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
                                binding.lineChart.animateXY(1500, 1500)
                            }
                        }
                    })
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun shiftDataset(result: List<ResultadoReporte>): List<ResultadoReporte> {
        return when {
            result.isNotEmpty() -> {
                val firstTimestamp = result.first().Day

                val mutableList = result.toMutableList()
                val iterate = mutableList.listIterator()
                while (iterate.hasNext()) {
                    var oldValue = iterate.next()
                    oldValue.Day = oldValue.Day - firstTimestamp
                }

                return mutableList.toList()
            }
            else -> {
                result
            }
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
