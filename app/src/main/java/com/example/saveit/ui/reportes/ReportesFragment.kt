package com.example.saveit.ui.reportes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.saveit.databinding.ReportesFragmentBinding

private const val ARG_TITLE = "Reportes"
class ReportesFragment: Fragment() {
    private var _binding: ReportesFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var title: String? = null
    private var listener: OnFragmentInteractionListener? = null
   // private lateinit var viewModel: ReportesViewModel

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
        _binding = ReportesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.byLocationButton.setOnClickListener {
            onByLocationButtonPressed()
        }

        binding.byDateButton.setOnClickListener {
            onByDateButtonPressed()
        }

        binding.byCategoryButton.setOnClickListener {
            onByCategoryButtonPressed()
        }
    }

   // override fun onActivityCreated(savedInstanceState: Bundle?) {
      //  super.onActivityCreated(savedInstanceState)
      //  viewModel = ViewModelProvider(this).get(ReportesViewModel::class.java)
        // TODO: Use the ViewModel
   // }

    fun onByLocationButtonPressed() {
        //listener?.showFragment(ByLocationFragment())
    }
    fun onByDateButtonPressed() {
        //listener?.showFragment(ByDateFragment())
    }
    fun onByCategoryButtonPressed() {
        //listener?.showFragment(ByCategoryFragment())
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
         * @return A new instance of fragment ReportesFragment.
         */
        @JvmStatic
        fun newInstance(title: String) =
            ReportesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }
}