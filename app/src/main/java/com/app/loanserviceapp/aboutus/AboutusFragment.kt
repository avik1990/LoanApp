package com.app.loanserviceapp.aboutus

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.loanserviceapp.R
import com.app.loanserviceapp.dashboard.adapter.LoanStatusAdpater
import com.app.loanserviceapp.databinding.AboutusFragmentBinding
import com.app.loanserviceapp.databinding.DashboardFragmentBinding
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_fragment.*

@AndroidEntryPoint
class AboutusFragment : Fragment() {

    companion object {
        fun newInstance() = AboutusFragment()
    }

    private lateinit var viewModel: AboutusViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var _binding: AboutusFragmentBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AboutusFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AboutusViewModel::class.java]

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Downloading")
        progressDialog.setMessage("Please Wait...")
        progressDialog.show()
        viewModel.getPageContent("ABOUT_US")

        fetchData()
    }

    private fun fetchData() {
        viewModel.responseStatus.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if (response.data.status == 1) {
                            _binding.textView2.text =
                                response.data.data.PageDetails.PagesDescription
                            _binding.textView.text = response.data.data.PageDetails.PagesTitle
                        } else {
                            Toast.makeText(
                                requireContext(),
                                response.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    progressDialog.dismiss()
                }
                is NetworkResult.Error -> {
                    Log.e("response", response.message.toString())
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

}