package com.example.saveit.ui.movimientos.actualizar

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.saveit.R
import com.example.saveit.databinding.ActualizarMovimientoFragmentBinding
import com.example.saveit.model.Movimiento
import com.example.saveit.viewmodel.MovimientoViewModel
import kotlinx.android.synthetic.main.actualizar_movimiento_fragment.*
import java.util.*

class ActualizarMovimientoFragment : Fragment() {
    private var _binding: ActualizarMovimientoFragmentBinding? = null
    private val binding get() = _binding!!

    private var listener: OnFragmentInteractionListener? = null

  //  private val args by navArgs<ActualizarMovimientoFragmentArgs>()

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ActualizarMovimientoFragmentBinding.inflate(inflater, container, false)

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

//        binding.updateFirstNameEt.setText(args.currentMovimiento.monto.toString())
//        binding.updateLastNameEt.setText(args.currentMovimiento.monto.toString())
//        binding.updateAgeEt.setText(args.currentMovimiento.monto.toString())

        binding.updateBtn.setOnClickListener {
            updateMovimiento()
        }

        return binding.root
    }

    private fun updateMovimiento() {
        val firstName = updateFirstName_et.text.toString()
        val lastName = updateLastName_et.text.toString()
        val age = updateAge_et.text

        if (inputCheck(firstName, lastName, age)) {
//            val updatedMovimiento = Movimiento(args.currentMovimiento.id, firstName.toDouble(), firstName.toInt(), firstName.toInt(), Date().time, firstName, firstName, firstName.toInt())

//            mMovimientoViewModel.updateMovimiento(updatedMovimiento)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_actualizarMovimientoFragment_to_listaMovimientosFragment2)
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
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
            ActualizarMovimientoFragment()
    }
}