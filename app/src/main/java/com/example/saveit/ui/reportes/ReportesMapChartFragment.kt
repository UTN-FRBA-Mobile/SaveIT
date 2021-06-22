package com.example.saveit.ui.reportes

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saveit.R
import com.example.saveit.data.Meses
import com.example.saveit.databinding.ReportesMapChartFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.ui.ahorro.AhorroAdapter
import com.example.saveit.viewmodel.MovimientoViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.reportes_fragment.view.*
import kotlinx.android.synthetic.main.reportes_map_chart_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class ReportesMapChartFragment : Fragment(), OnMapReadyCallback {
    private var _binding: ReportesMapChartFragmentBinding? = null
    private lateinit var mMap: GoogleMap
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMovimientoViewModel: MovimientoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        _binding = ReportesMapChartFragmentBinding.inflate(inflater, container, false)

        createMapFragment()

        val adapter = AhorroAdapter()


        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        loadYearsAndMonths()
        binding.botonGenerarReporte.setOnClickListener {
            val anio = binding.seleccionAnio.findViewById<TextInputLayout>(R.id.seleccion_anio).editText!!.text.toString()
            val mes = binding.seleccionMes.findViewById<TextInputLayout>(R.id.seleccion_mes).editText!!.text.toString()

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
                    cargarMapa(user)

                })
            }
            else {
                Toast.makeText(requireContext(), "Debe seleccionar un año", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun cargarMapa(movimientos: List<Movimiento>?) {
        if (movimientos != null) {
            for(movimiento in movimientos){
                createMarker(movimiento)
            }
        }

    }

    private fun createMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.myGoogleMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val coordinates = LatLng(-34.605810144384535, -58.43575288699898)
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            4000,
            null
        )

    }

    private fun createMarker(movimiento: Movimiento) {


        if(movimiento.tipoMovimiento == 0){
            val coordinates = LatLng(-34.605810144384535, -58.43575288699898)
            val marker = MarkerOptions().position(coordinates).title(movimiento.descripcion + " $" + movimiento.monto.toString()).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))//Verde para Ingresos
                mMap.addMarker(marker)
        }else{
            val coordinates = LatLng(-34.60118340633596, -58.432715556780785)
            val marker = MarkerOptions().position(coordinates).title(movimiento.descripcion + " $" + movimiento.monto.toString()).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))//Rojo para gastos
                mMap.addMarker(marker)
        }




    }

    fun loadYearsAndMonths() {
        val asc = Array(10) { i -> (Calendar.getInstance().get(Calendar.YEAR)- i).toString() }
        val itemsSeleccionAnios = asc.map { it }
        val adapterSeleccionAnios = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionAnios)
        (binding.seleccionAnio.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionAnios)

        val itemsSeleccionMeses = Meses.values().map { it.descripcion }
        val adapterSeleccionMeses = ArrayAdapter(requireContext(), R.layout.lista_items, itemsSeleccionMeses)
        (binding.seleccionMes.editText as? AutoCompleteTextView)?.setAdapter(adapterSeleccionMeses)
    }


    private fun getFirstDayOfMonth(year: Int, month: Int): Long {
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

    private fun getLastDayOfMonth(year: Int, month: Int): Long {
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

    private fun formatDate(fecha: Long): String {
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }


}
