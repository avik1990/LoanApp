package com.app.loanserviceapp.dashboard

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.loanserviceapp.R
import com.app.loanserviceapp.dashboard.adapter.LoanStatusAdpater
import com.app.loanserviceapp.databinding.DashboardFragmentBinding
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.readString
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class Dashboard : Fragment(), LoanStatusAdpater.onRowItemSelected {

    companion object {
        fun newInstance() = Dashboard()
    }

    private lateinit var viewModel: DashboardViewModel
    private lateinit var _binding: DashboardFragmentBinding
    private val binding get() = _binding!!
    var userId:String=""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]



        runBlocking {
            val getUserName = context?.readString(Constants.USER_ID)
            userId = getUserName?.first().toString()
        }
    }

    override fun onResume() {
        super.onResume()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Downloading")
        progressDialog.setMessage("Please Wait...")
        progressDialog.show()
        viewModel.getLoanStatusList(userId)
        viewModel.getPaymentKeys()
        fetchData()
        fetchPaymentData()
    }

    private fun fetchData() {
        viewModel.responseStatus.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if(response.data.status==1) {
                            val obj_adapter = LoanStatusAdpater(this,response.data.data.MyApplications)
                            loanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                            loanList.adapter = obj_adapter

                            _binding.btnLoanApply.setOnClickListener {
                                if(response.data.data.hasPendingLoan.equals("Y")){
                                    Toast.makeText(requireContext(),"You already have pending loan.You can not apply now",Toast.LENGTH_LONG).show()
                                }else{
                                    findNavController().navigate(R.id.action_DashboardFragment_to_LoanApprovalFragment)
                                }
                            }
                        } else{
                            Toast.makeText(
                                requireContext(),
                                response.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    //progressDialog.dismiss()
                }
                is NetworkResult.Error -> {
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
    private fun fetchPaymentData() {
        viewModel.responsePayment.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if(response.data.status==1) {
                            viewModel.saveMerchnatKey(response.data.data.ApiData.MerchantKey)
                            viewModel.saveMerchantSalt(response.data.data.ApiData.MerchantSalt)
                        } else{
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
    override fun getSelectedItem(id: String, processingFees: String) {
        try {
            val action = DashboardDirections.actionDashboardFragmentToPayment(
                id, processingFees)
            findNavController().navigate(action)
        }catch (e: Exception){}
    }
}