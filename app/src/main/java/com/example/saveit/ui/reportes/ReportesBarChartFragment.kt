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
import com.example.saveit.data.CategoriasIngreso
import com.example.saveit.data.TipoMovimiento
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class ReportesBarChartFragment : Fragment() {
    private var _binding: ReportesBarChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMovimientoViewModel: MovimientoViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesBarChartFragmentBinding.inflate(inflater, container, false)

        var itemsTipoDeMovimiento = TipoMovimiento.values().map { it.descripcion }
        val adapterTipoDeMovimiento = ArrayAdapter(requireContext(), R.layout.lista_items, itemsTipoDeMovimiento)
        (binding.tipoMovimientoBarChart.editText as? AutoCompleteTextView)?.setAdapter(adapterTipoDeMovimiento)


        var chipsEgresos = binding.barChipsEgresos
        var chipsIngrsos = binding.barChipsIngresos
        chipsEgresos.setVisibility(View.INVISIBLE)
        chipsIngrsos.setVisibility(View.INVISIBLE)

        val adapter = ListaMovimientosAdapter()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)
        mMovimientoViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            if (user.size == 0)
                Toast.makeText(requireContext(), "No hay movimientos cargados", Toast.LENGTH_LONG).show()

            adapter.setData(user)
        })
        loadYearsAndMonths()

        binding.botonGenerarReporte.setOnClickListener {
            val anio =
                binding.seleccionAnioBarChart.findViewById<TextInputLayout>(R.id.seleccion_anio_barChart).editText!!.text.toString()
            val mes =
                binding.seleccionMesBarChart.findViewById<TextInputLayout>(R.id.seleccion_mes_barChart).editText!!.text.toString()
            val tipoMov =
                binding.tipoMovimientoBarChart.findViewById<TextInputLayout>(R.id.tipoMovimientoBarChart).editText!!.text.toString()

            if (tipoMov != "") {
                if (anio != "") {
                    var desde: Long
                    var hasta: Long

                    if (mes != "") {
                        desde =
                            getFirstDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion(mes))
                        hasta =
                            getLastDayOfMonth(Integer.parseInt(anio), Meses.getByDescripcion(mes))
                    } else {
                        desde = getFirstDayOfMonth(
                            Integer.parseInt(anio),
                            Meses.getByDescripcion("Enero")
                        )
                        hasta = getLastDayOfMonth(
                            Integer.parseInt(anio),
                            Meses.getByDescripcion("Diciembre")
                        )
                    }

                    mMovimientoViewModel.readAllDataBetween(desde, hasta)
                        .observe(viewLifecycleOwner, Observer { user ->
                            if (user.size == 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "No hay movimientos cargados",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            cargarGrafico(user)
                        })
                } else {
                    Toast.makeText(requireContext(), "Debe seleccionar un año", Toast.LENGTH_LONG)
                        .show()
                }
            }else{
                Toast.makeText(requireContext(), "Debe seleccionar un Tipo de Movimiento", Toast.LENGTH_LONG).show()

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
        (binding.seleccionAnioBarChart.editText as? AutoCompleteTextView)?.setText("")
        (binding.seleccionMesBarChart.editText as? AutoCompleteTextView)?.setText("")
    }


    private fun cargarGrafico(movimientos : List<Movimiento> ) {

      var barChart = binding.barChart
        val tipoMov = binding.tipoMovimientoBarChart.findViewById<TextInputLayout>(R.id.tipoMovimientoBarChart).editText!!.text.toString()

        if(tipoMov == "Ingreso") {
            var chipsEgresos = binding.barChipsEgresos
            var chipsIngrsos = binding.barChipsIngresos
            chipsEgresos.setVisibility(View.INVISIBLE)
            chipsIngrsos.setVisibility(View.VISIBLE)

            var montos = ArrayList<BarEntry>()

            val categorias = CategoriasIngreso.values()
            var total_x_categoria = FloatArray(CategoriasIngreso.values().size)
            for (movimiento in movimientos) {
                if(movimiento.tipoMovimiento == 0) {//Si es un Ingreso
                    total_x_categoria[movimiento.categoria - 1] =
                        (total_x_categoria[movimiento.categoria - 1] + movimiento.monto).toFloat()
                }
            }



            for (i in 1..categorias.size) {
                montos.add(BarEntry(i.toFloat(), total_x_categoria[i - 1]))
            }

            var barDataSet = BarDataSet(montos, "Categorias")
            barDataSet.setColors(
                Color.rgb(255, 23, 68),//Ahorro
                Color.rgb(255, 214, 0),//Plazo Fijo
                Color.rgb(213, 0, 249),//Prestamo
                Color.rgb(101, 31, 255),//Sueldo
                Color.rgb(48, 79, 254),//Ventas
                Color.rgb(245, 127, 23),//Otro
            )


            var barData = BarData()
            barData.addDataSet(barDataSet)
            barChart.setData(barData)
            barChart.animateXY(1500, 1500)

            binding.barChart.notifyDataSetChanged()
            binding.barChart.invalidate()

        }else{
            var chipsEgresos = binding.barChipsEgresos
            var chipsIngrsos = binding.barChipsIngresos
            chipsEgresos.setVisibility(View.VISIBLE)
            chipsIngrsos.setVisibility(View.INVISIBLE)

            var montos = ArrayList<BarEntry>()

            val categorias = CategoriasGasto.values()
            var total_x_categoria = FloatArray(CategoriasGasto.values().size)
            for (movimiento in movimientos) {
                if(movimiento.tipoMovimiento == 1) {//Si es un egreso
                    total_x_categoria[movimiento.categoria - 1] =
                        (total_x_categoria[movimiento.categoria - 1] + movimiento.monto).toFloat()
                }
            }



            for (i in 1..categorias.size) {
                montos.add(BarEntry(i.toFloat(), total_x_categoria[i - 1]))
            }

            var barDataSet = BarDataSet(montos, "Categorias")
            barDataSet.setColors(
                Color.rgb(255, 23, 68),//Ahorro
                Color.rgb(255, 214, 0),//Alquiler
                Color.rgb(213, 0, 249),//Casa
                Color.rgb(101, 31, 255),//Comida
                Color.rgb(48, 79, 254),//Delivery
                Color.rgb(245, 127, 23),//Educacion
                Color.rgb(41, 182, 246),//Gasolina
                Color.rgb(0, 131, 14),//Personales
                Color.rgb(38, 166, 154)//Servicios
                , Color.rgb(0, 77, 64)//Otros
            )

            var barData = BarData()
            barData.addDataSet(barDataSet)
            barChart.setData(barData)
            barChart.animateXY(1500, 1500)

            binding.barChart.notifyDataSetChanged()
            binding.barChart.invalidate()

        }

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