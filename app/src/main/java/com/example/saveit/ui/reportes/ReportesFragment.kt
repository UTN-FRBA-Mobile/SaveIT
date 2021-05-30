package com.example.saveit.ui.reportes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.saveit.NavegacionInterface
import com.example.saveit.databinding.ReportesFragmentBinding
import com.example.saveit.ui.ahorro.AhorroFragment

class ReportesFragment : Fragment() {
    private var _binding: ReportesFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = ReportesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          binding.mButton.setText("mButton")

        binding.pieChartButton.setOnClickListener {
            onButtonPieChartPressed()
        }

        binding.barChartButton.setOnClickListener {
            onButtonBarChartPressed()
        }

        binding.mapChartButton.setOnClickListener {
            onButtonMapChartPressed()
        }
    }

    fun onButtonPieChartPressed() {
        (activity as NavegacionInterface).showFragment(ReportesPieChartFragment(), true)
        //   listener?.showFragment(AhorroFragment())
    }


    fun onButtonBarChartPressed() {
        (activity as NavegacionInterface).showFragment(ReportesBarChartFragment(), true)
        //   listener?.showFragment(AhorroFragment())
    }

    fun onButtonMapChartPressed() {
        (activity as NavegacionInterface).showFragment(ReportesMapChartFragment(), true)
        //   listener?.showFragment(AhorroFragment())
    }
    /*override fun onAttach(context: Context) {
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
            ReportesFragment()
    }
}
