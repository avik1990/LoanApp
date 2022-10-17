package com.app.loanserviceapp.loanapackages

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.databinding.LoanApprovalFragmentBinding
import com.app.loanserviceapp.loanapackages.model.PackagesArr
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanApprovalFragment : Fragment() {

    companion object {
        fun newInstance() = LoanApprovalFragment()
    }

    private lateinit var viewModel: LoanApprovalViewModel
    private lateinit var _binding: LoanApprovalFragmentBinding
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog
    val list: ArrayList<String> = ArrayList()

    var packageId=""
    var insuranceFees=""
    var processingFees=""
    var packagename=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoanApprovalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoanApprovalViewModel::class.java]

        _binding.btnLoanApplyLoan.setOnClickListener {
            if (processingFees.isNotEmpty()) {
                val action= LoanApprovalFragmentDirections.actionLoanApprovalFragmentToDocumenFragment(
                    packageId,
                    insuranceFees,
                    processingFees,
                    packagename)
                findNavController().navigate(action)
            }
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Downloading")
        progressDialog.setMessage("Please Wait...")

    }

    override fun onResume() {
        super.onResume()
        getValidated()
    }

    private fun getValidated() {
        progressDialog.show()
        viewModel.getPackagesData()
        fetchData()
    }

    private fun fetchData() {
        viewModel.responsePa.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if (response.data.status == 1) {
                            if (response.data.data.PackagesArr.isNotEmpty()) {
                                inflateSpinner(response.data?.data?.PackagesArr)
                            }
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
                    // _binding.pbDog.visibility = View.VISIBLE
                    //  progressDialog.show()
                }
            }
        }
    }

    private fun inflateSpinner(packagesArr: List<PackagesArr>) {
        list.add("Select Packages")
        for (i in 0 until packagesArr.size) {
            list.add(packagesArr[i].PackageName)
        }
        if (list.size > 0) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, list
            )
            _binding.spnPackages?.adapter = adapter

            _binding.spnPackages?.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    if (position != 0) {
                        packagename=packagesArr[position - 1].PackageName
                        packageId=packagesArr[position - 1].PackageID
                        insuranceFees=packagesArr[position - 1].InsuranceFee
                        processingFees=packagesArr[position - 1].ProcessingFee
                        _binding.etInsurance.setText(insuranceFees)
                        _binding.etProcessingFees.setText(processingFees)
                    }else{
                        packagename=""
                        packageId=""
                        insuranceFees=""
                        processingFees=""
                        _binding.etInsurance.setText("")
                        _binding.etProcessingFees.setText("")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }



}


