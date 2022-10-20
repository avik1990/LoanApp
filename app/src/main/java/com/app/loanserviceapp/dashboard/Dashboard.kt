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
class Dashboard : Fragment() {

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

        _binding.btnLoanApply.setOnClickListener {
            findNavController().navigate(R.id.action_DashboardFragment_to_LoanApprovalFragment)
        }

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
        fetchData()
    }

    private fun fetchData() {
        viewModel.responseStatus.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if(response.data.status==1) {
                            val obj_adapter = LoanStatusAdpater(response.data.data.MyApplications)
                            loanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                            loanList.adapter = obj_adapter
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


}