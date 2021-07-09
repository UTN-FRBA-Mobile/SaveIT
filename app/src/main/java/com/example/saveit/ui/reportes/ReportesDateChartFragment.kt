package com.example.saveit.ui.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.saveit.R
import com.example.saveit.data.*
import com.example.saveit.databinding.ReportesDateChartFragmentBinding
import com.example.saveit.data.CategoriasIngreso
import com.example.saveit.data.CategoriasGasto

class ReportesDateChartFragment: Fragment()  {
    private var _binding: ReportesDateChartFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesDateChartFragmentBinding.inflate(inflater, container, false)

        var itemsTipoDeMovimiento = TipoMovimiento.values().map { it.descripcion }
        val adapterTipoDeMovimiento = ArrayAdapter(requireContext(), R.layout.lista_items, itemsTipoDeMovimiento)
        (binding.tipoMovimiento.editText as? AutoCompleteTextView)?.setAdapter(adapterTipoDeMovimiento)

        var adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, listOf<String>())
        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)
        binding.categoria.isEnabled = false

        var itemsMedioPago = MedioPago.values().map { it.descripcion }
        itemsMedioPago = itemsMedioPago.toMutableList()
        itemsMedioPago.add(0, "Todos")
        val adapterMedioPago = ArrayAdapter(requireContext(), R.layout.lista_items, itemsMedioPago)
        (binding.medioDePago.editText as? AutoCompleteTextView)?.setAdapter(adapterMedioPago)
        binding.medioDePago.isEnabled = false

        val adapterPeriodosDeTiempo = ArrayAdapter(requireContext(), R.layout.lista_items, PeriodosDeTiempo.values().map { it.descripcion })
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriodosDeTiempo)
        binding.periodoDeTiempo.isEnabled = false

        (binding.tipoMovimiento.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue: String? = adapterTipoDeMovimiento.getItem(position)
                if (selectedValue != null) {
                    if (selectedValue == TipoMovimiento.INGRESO.descripcion) {

                        adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, listOf<String>())
                        var itemsCategorias = CategoriasIngreso.values().map { it.descripcion }
                        itemsCategorias = itemsCategorias.toMutableList()
                        itemsCategorias.add(0, "Todas")
                        adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemsCategorias)
                        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)
                        (binding.categoria.editText as? AutoCompleteTextView)?.setText("")
                        binding.categoria.isEnabled = true
                        binding.medioDePago.isEnabled = false
                        binding.periodoDeTiempo.isEnabled = false

                    } else if (selectedValue == TipoMovimiento.EGRESO.descripcion) {

                        adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, listOf<String>())
                        var itemsCategorias = CategoriasGasto.values().map { it.descripcion }
                        itemsCategorias = itemsCategorias.toMutableList()
                        itemsCategorias.add(0, "Todas")
                        adapterCategoria = ArrayAdapter(requireContext(), R.layout.lista_items, itemsCategorias)
                        (binding.categoria.editText as? AutoCompleteTextView)?.setAdapter(adapterCategoria)
                        (binding.categoria.editText as? AutoCompleteTextView)?.setText("")
                        binding.categoria.isEnabled = true
                        binding.medioDePago.isEnabled = false
                        binding.periodoDeTiempo.isEnabled = false

                    }
                }
            }

        (binding.categoria.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue: String? = adapterCategoria.getItem(position)
                if (selectedValue != null) {

                    (binding.medioDePago.editText as? AutoCompleteTextView)?.setText("")
                    binding.medioDePago.isEnabled = true
                    binding.periodoDeTiempo.isEnabled = false

                }
            }

        (binding.medioDePago.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue: String? = adapterMedioPago.getItem(position)
                if (selectedValue != null) {

                    (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setText("")
                    binding.periodoDeTiempo.isEnabled = true

                }
            }

        binding.botonGenerarReporte.setOnClickListener {
            openReport()
        }

        binding.botonLimpiarFormulario.setOnClickListener {
            cleanForm()
        }

        return binding.root
    }

    private fun cleanForm() {
        (binding.tipoMovimiento.editText as? AutoCompleteTextView)?.setText("")
        (binding.medioDePago.editText as? AutoCompleteTextView)?.setText("")
        (binding.categoria.editText as? AutoCompleteTextView)?.setText("")
        (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.setText("")
        binding.medioDePago.isEnabled = false
        binding.categoria.isEnabled = false
        binding.periodoDeTiempo.isEnabled = false
    }

    private fun openReport() {
        val tipoMovimiento = (binding.tipoMovimiento.editText as? AutoCompleteTextView)?.text
        val medioPagoSelec = (binding.medioDePago.editText as? AutoCompleteTextView)?.text
        val categoriaSelec = (binding.categoria.editText as? AutoCompleteTextView)?.text
        val periodoSelec = (binding.periodoDeTiempo.editText as? AutoCompleteTextView)?.text

        if (tipoMovimiento.isNullOrEmpty()
            || medioPagoSelec.isNullOrEmpty()
            || categoriaSelec.isNullOrEmpty()
            || periodoSelec.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Por favor, selecciona todos los campos", Toast.LENGTH_LONG).show()
        }
        else{
            val seleccion = arrayListOf(tipoMovimiento.toString(),
                medioPagoSelec.toString(),
                categoriaSelec.toString(),
                periodoSelec.toString())

            val action = ReportesDateChartFragmentDirections.actionReportesDateChartFragmentToActualDateChartFragment(seleccion.toTypedArray())
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
