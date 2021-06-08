package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.saveit.NavegacionInterface
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.databinding.ReportesDateChartFragmentBinding

class ReportesDateChartFragment: Fragment()  {
    private var _binding: ReportesDateChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesDateChartFragmentBinding.inflate(inflater, container, false)

        var itemsMedioPago = MedioPago.values().map { it.descripcion }
        itemsMedioPago = itemsMedioPago.toMutableList()
        itemsMedioPago.add(0, "Todos")
        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMedioPago)
        (binding.medioDePago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

        var itemsCategorias = Categoria.values().map { it.descripcion }
        itemsCategorias = itemsCategorias.toMutableList()
        itemsCategorias.add(0, "Todos")
        val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemsCategorias)
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

        val itemsMonedas = Moneda.values().map { it.descripcion }
        val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMonedas)
        (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)

        val itemsPeriodosDeTiempo = PeriodosDeTiempo.values().map { it.descripcion }
        val adapterPeriodosDeTiempo = ArrayAdapter(requireContext(), R.layout.lista_items, itemsPeriodosDeTiempo)
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriodosDeTiempo)

        (binding.medioDePago.editText as? AutoCompleteTextView)?.setText(adapterMedioPago.getItem(0).toString(), false)
        (binding.categoria.editText as? AutoCompleteTextView)?.setText(adapterCategoria.getItem(0).toString(), false)
        (binding.moneda.editText as? AutoCompleteTextView)?.setText(adapterMonedas.getItem(0).toString(), false)
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setText(adapterPeriodosDeTiempo.getItem(0).toString(), false)

        binding.botonGenerarReporte.setOnClickListener {
            openReport()
        }

        binding.botonLimpiarFormulario.setOnClickListener {
            cleanForm()
        }

        return binding.root
    }

    private fun cleanForm() {
        (binding.medioDePago.editText as? AutoCompleteTextView)?.setText("")
        (binding.categoria.editText as? AutoCompleteTextView)?.setText("")
        (binding.moneda.editText as? AutoCompleteTextView)?.setText("")
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setText("")
    }

    private fun openReport() {
        val medioPagoSelec = (binding.medioDePago.editText as? AutoCompleteTextView)?.text
        val categoriaSelec = (binding.categoria.editText as? AutoCompleteTextView)?.text
        val monedaSelec = (binding.moneda.editText as? AutoCompleteTextView)?.text
        val periodoSelec = (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.text

        if (medioPagoSelec.isNullOrEmpty()
            || categoriaSelec.isNullOrEmpty()
            || monedaSelec.isNullOrEmpty()
            || periodoSelec.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Por favor, selecciona todos los campos", Toast.LENGTH_LONG).show()
        }
        else{
            val seleccion = arrayListOf(medioPagoSelec?.toString(),
                categoriaSelec.toString(),
                monedaSelec.toString(),
                periodoSelec.toString())

            var actualdatechartfragment = ActualDateChartFragment.newInstance(seleccion)
            (activity as NavegacionInterface).showFragment(actualdatechartfragment, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
