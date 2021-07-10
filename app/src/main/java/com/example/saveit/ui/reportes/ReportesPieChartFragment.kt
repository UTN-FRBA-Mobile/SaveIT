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
import com.example.saveit.data.TipoMovimiento
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


        var itemsTipoDeMovimiento = TipoMovimiento.values().map { it.descripcion }
        val adapterTipoDeMovimiento = ArrayAdapter(requireContext(), R.layout.lista_items, itemsTipoDeMovimiento)
        (binding.tipoMovimientoPieChart.editText as? AutoCompleteTextView)?.setAdapter(adapterTipoDeMovimiento)

        var chipsEgresos = binding.pieChipsEgresos
        var chipsIngrsos = binding.pieChipsIngresos
        chipsEgresos.setVisibility(View.INVISIBLE)
        chipsIngrsos.setVisibility(View.INVISIBLE)

        val adapter = ListaMovimientosAdapter()

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        loadYearsAndMonths()

        binding.botonGenerarReporte.setOnClickListener {
            val anio = binding.seleccionAnioPieChart.findViewById<TextInputLayout>(R.id.seleccion_anio_pieChart).editText!!.text.toString()
            val mes = binding.seleccionMesPieChart.findViewById<TextInputLayout>(R.id.seleccion_mes_pieChart).editText!!.text.toString()
            val tipoMov = binding.tipoMovimientoPieChart.findViewById<TextInputLayout>(R.id.tipoMovimientoPieChart).editText!!.text.toString()

            if (tipoMov != "") {
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
        (binding.seleccionMesPieChart.editText as? AutoCompleteTextView)?.setText("")
        (binding.seleccionAnioPieChart.editText as? AutoCompleteTextView)?.setText("")
    }


    private fun cargarGrafico(movimientos : List<Movimiento> ) {
        var pieChart = binding.pieChart
        val tipoMov = binding.tipoMovimientoPieChart.findViewById<TextInputLayout>(R.id.tipoMovimientoPieChart).editText!!.text.toString()

        if(tipoMov == "Ingreso"){
            var chipsEgresos = binding.pieChipsEgresos
            var chipsIngrsos = binding.pieChipsIngresos
            chipsEgresos.setVisibility(View.INVISIBLE)
            chipsIngrsos.setVisibility(View.VISIBLE)
            val categorias = CategoriasIngreso.values()
            var cero = 0
            var total_x_categoria =  FloatArray(CategoriasIngreso.values().size)

            for(movimiento in movimientos){
                if(movimiento.tipoMovimiento == 0) {//Si es un ingreso
                    total_x_categoria[movimiento.categoria - 1] =
                        (total_x_categoria[movimiento.categoria - 1] + movimiento.monto).toFloat()
                }
            }

            var categoriasPieEntry = ArrayList<PieEntry>()
            var totalGastos:Double = 0.0

            for(i in 0..categorias.size-1) {
                    totalGastos = totalGastos + total_x_categoria[i]

            }


            for(i in 0..categorias.size-1) {
                if(total_x_categoria[i] > 0){
                    categoriasPieEntry.add(PieEntry(total_x_categoria[i]*100/totalGastos.toFloat(),CategoriasIngreso.getByValor(i+1)))


                }else{
                    categoriasPieEntry.add(PieEntry(total_x_categoria[i]*100/totalGastos.toFloat(),""))
                }
            }

            var pieDataSet = PieDataSet(categoriasPieEntry, "Categorias")
            pieDataSet.setColors(
                Color.rgb(255, 23, 68),//Ahorro
                Color.rgb(255, 214, 0),//Plazo Fijo
                Color.rgb(213, 0, 249),//Prestamo
                Color.rgb(101, 31, 255),//Sueldo
                Color.rgb(48, 79, 254),//Ventas
                Color.rgb(245, 127, 23),//Otro
            )




            pieDataSet.setValueTextSize(16F)

            pieDataSet.setValueTextColors(listOf(Color.BLACK))

            var pieData = PieData(pieDataSet)

            pieChart.setData(pieData)
            pieChart.setCenterText("Categorias")
            pieChart.animateXY(1500, 1500)

            binding.pieChart.notifyDataSetChanged()
            binding.pieChart.invalidate()
        }else{
            val categorias = CategoriasGasto.values()
            var cero = 0
            var total_x_categoria =  FloatArray(CategoriasGasto.values().size)

            var chipsEgresos = binding.pieChipsEgresos
            var chipsIngrsos = binding.pieChipsIngresos
            chipsEgresos.setVisibility(View.VISIBLE)
            chipsIngrsos.setVisibility(View.INVISIBLE)


            for(movimiento in movimientos){
                if(movimiento.tipoMovimiento == 1) {//Si es un egreso
                    total_x_categoria[movimiento.categoria - 1] =
                        (total_x_categoria[movimiento.categoria - 1] + movimiento.monto).toFloat()
                }
            }

            var categoriasPieEntry = ArrayList<PieEntry>()
            var totalGastos:Double = 0.0

            for(i in 0..categorias.size-1) {
                    totalGastos = totalGastos + total_x_categoria[i]
            }


            for(i in 0..categorias.size-1) {
                if(total_x_categoria[i] > 0){
                    categoriasPieEntry.add(PieEntry(total_x_categoria[i]*100/totalGastos.toFloat(),CategoriasGasto.getByValor(i+1)))
                }else{
                    categoriasPieEntry.add(PieEntry(total_x_categoria[i]*100/totalGastos.toFloat(),""))
                }
            }


            var pieDataSet = PieDataSet(categoriasPieEntry, "Categorias")
            pieDataSet.setColors(
                Color.rgb(255, 23, 68),//Ahorro
                Color.rgb(255, 214, 0),//Alquiler
                Color.rgb(213, 0, 249),//Casa
                Color.rgb(101, 31, 255),//Comida
                Color.rgb(48, 79, 254),//Delivery
                Color.rgb(245, 127, 23),//Educacion
                Color.rgb(41, 182, 246),//Gasolina
                Color.rgb(0, 131, 14),//Personales
                Color.rgb(38, 166, 154)//Servicios
                ,Color.rgb(0, 77, 64)//Otros
            )


            pieDataSet.setValueTextSize(16F)

            pieDataSet.setValueTextColors(listOf(Color.BLACK))

            var pieData = PieData(pieDataSet)

            pieChart.setData(pieData)
            pieChart.setCenterText("Categorias")
            pieChart.animateXY(1500, 1500)

            binding.pieChart.notifyDataSetChanged()
            binding.pieChart.invalidate()
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
        (binding.seleccionAnioPieChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMesPieChart.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)


    }


}