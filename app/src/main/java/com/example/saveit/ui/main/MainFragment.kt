package com.example.saveit.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.saveit.R
//import androidx.lifecycle.ViewModelProvider
import com.example.saveit.databinding.MainFragmentBinding
import com.example.saveit.ui.ahorro.AhorroFragment
import com.example.saveit.ui.movimientos.agregar.AgregarMovimientosFragment
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragment
import com.example.saveit.ui.reportes.ReportesFragment

private const val ARG_TITLE = "title"
class MainFragment: Fragment() {
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var title: String? = null
    private var listener: OnFragmentInteractionListener? = null
   // private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.ahorroButton.text = "Ahorro"
        binding.ahorroButton.setOnClickListener {
            onButtonAhorroPressed()
        }
        //binding.reportesButton.text = "Reportes"
        binding.reportesButton.setOnClickListener {
            onButtonReportesPressed()
        }
        //binding.listaMovimientosButton.text = "Lista Movimientos"
        binding.listaMovimientosButton.setOnClickListener {
            onButtonlistaMovimientosPressed()
        }
        //binding.agregarMovimientosButton.text = "Agregar Movimientos"
        binding.agregarMovimientosButton.setOnClickListener {
            onButtonagregarMovimientosPressed()
        }
    }

   // override fun onActivityCreated(savedInstanceState: Bundle?) {
      //  super.onActivityCreated(savedInstanceState)
      //  viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
   // }

    fun onButtonAhorroPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_ahorroFragment)
    }
    fun onButtonReportesPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_reportesFragment)
    }
    fun onButtonlistaMovimientosPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_listaMovimientosFragment2)
    }
    fun onButtonagregarMovimientosPressed() {
        findNavController().navigate(R.id.action_mainFragment_to_agregarMovimientosFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Title.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance(title: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }
}