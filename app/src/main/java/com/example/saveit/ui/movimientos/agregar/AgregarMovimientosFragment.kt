package com.example.saveit.ui.movimientos.agregar

//import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.saveit.data.Movimiento
import com.example.saveit.data.MovimientoViewModel
import com.example.saveit.databinding.AgregarMovimientosFragmentBinding
import java.util.*

class AgregarMovimientosFragment: Fragment() {
    private var _binding: AgregarMovimientosFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var listener: OnFragmentInteractionListener? = null

    private lateinit var mMovimientoViewModel: MovimientoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = AgregarMovimientosFragmentBinding.inflate(inflater, container, false)

        mMovimientoViewModel = ViewModelProvider(this).get(MovimientoViewModel::class.java)

        binding.addBtn.setOnClickListener {
            insertDataToDataBase()
        }

        return binding.root
    }

    private fun insertDataToDataBase() {
        val firstName = binding.addFirstNameEt.text.toString()
        val lastName = binding.addLastNameEt.text.toString()
        val age = binding.addAgeEt.text

        if (inputCheck(firstName, lastName, age)) {
            val movimiento = Movimiento(0, firstName.toDouble(), firstName.toInt(), firstName.toInt(), Date().time, firstName, firstName, firstName.toInt())

            // Add Data to Database
            mMovimientoViewModel.addMovimiento(movimiento)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//          binding.textField.setTextColor(Color.WHITE)
    }

   /* override fun onAttach(context: Context) {
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


    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     *//*
    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment StatusUpdate.
         */
        @JvmStatic
        fun newInstance() =
            AgregarMovimientosFragment()
    }
}