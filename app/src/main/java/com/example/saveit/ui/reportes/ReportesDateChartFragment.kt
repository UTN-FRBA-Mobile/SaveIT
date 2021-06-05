package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.saveit.R
import com.example.saveit.data.Categoria
import com.example.saveit.data.MedioPago
import com.example.saveit.data.Moneda
import com.example.saveit.data.PeriodosDeTiempo
import com.example.saveit.databinding.ReportesDateChartFragmentBinding

class ReportesDateChartFragment: Fragment()  {
    private var _binding: ReportesDateChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesDateChartFragmentBinding.inflate(inflater, container, false)

        val itemsMedioPago = MedioPago.values().map { it.descripcion }
        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMedioPago)
        (binding.medioDePago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)

        val itemsCategorias = Categoria.values().map { it.descripcion }
        val adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemsCategorias)
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)

        val itemsMonedas = Moneda.values().map { it.descripcion }
        val adapterMonedas = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMonedas)
        (binding.moneda.editText as? AutoCompleteTextView)?.setAdapter(adapterMonedas)

        val itemsPeriodosDeTiempo = PeriodosDeTiempo.values().map { it.descripcion }
        val adapterPeriodosDeTiempo = ArrayAdapter(requireContext(), R.layout.lista_items, itemsPeriodosDeTiempo)
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriodosDeTiempo)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}