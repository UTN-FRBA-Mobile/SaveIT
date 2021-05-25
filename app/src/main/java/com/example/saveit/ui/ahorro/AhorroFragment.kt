package com.example.saveit.ui.ahorro

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saveit.databinding.AhorroFragmentBinding
import java.time.LocalDateTime

class AhorroFragment: Fragment() {
        private var _binding: AhorroFragmentBinding? = null
        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        private var listener: OnFragmentInteractionListener? = null

        val ahorrosList = listOf<Ahorro>(
            Ahorro(15000f,true, LocalDateTime.now()),
            Ahorro(5000f,false, LocalDateTime.now()),
            Ahorro(25000f,true, LocalDateTime.now()),
            Ahorro(15000f,true, LocalDateTime.now()),
            Ahorro(5000f,false, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(15000f,false, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(15000f,false, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(15000f,false, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(15000f,false, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(18700f,true, LocalDateTime.now()),
            Ahorro(15000f,false, LocalDateTime.now()),
            Ahorro(8200f,false, LocalDateTime.now()),
            Ahorro(16000f,true, LocalDateTime.now())
        )

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            _binding = AhorroFragmentBinding.inflate(inflater, container, false)
            initRecycler()
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.montoTotal.text = "Total: " + calcularMontoTotal().toString()
//          binding.textField.setTextColor(Color.WHITE)
        }

    private fun calcularMontoTotal(): Any {
        val ingresos = ahorrosList.filter { ahorro -> ahorro.ingreso }.map { ahorro -> ahorro.monto }.sum()
        val egresos = ahorrosList.filter { ahorro -> !ahorro.ingreso }.map { ahorro -> ahorro.monto }.sum()
        return ingresos - egresos
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
             * @return A new instance of fragment StatusUpdate.
             */
            @JvmStatic
            fun newInstance() =
                AhorroFragment()
        }
        fun initRecycler(){
            binding.rvAhorro.layoutManager = LinearLayoutManager(context)
            val adapter = AhorroAdapter(ahorrosList)
            binding.rvAhorro.adapter = adapter

        }

}