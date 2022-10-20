package com.app.loanserviceapp.terms

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.loanserviceapp.R
import com.app.loanserviceapp.contact.ContactViewModel
import com.app.loanserviceapp.databinding.ContactFragmentBinding
import com.app.loanserviceapp.databinding.TermsFragmentBinding
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : Fragment() {

    companion object {
        fun newInstance() = TermsFragment()
    }

    private lateinit var viewModel: TermsViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var _binding: TermsFragmentBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TermsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[TermsViewModel::class.java]

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Downloading")
        progressDialog.setMessage("Please Wait...")
        progressDialog.show()
        viewModel.getPageContent("CONTACT_US")

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