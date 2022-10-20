package com.app.loanserviceapp.password

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.databinding.PasswordPinFragmentBinding
import com.app.loanserviceapp.register.RegisterDirections
import com.app.loanserviceapp.utils.Constants.Companion.IS_LOGGED_IN
import com.app.loanserviceapp.utils.Datastore
import com.app.loanserviceapp.utils.Datastore.writeBool
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class PasswordPin : Fragment() {

    companion object {
        fun newInstance() = PasswordPin()
    }

    private lateinit var viewModel: PasswordPinViewModel
    private lateinit var _binding: PasswordPinFragmentBinding
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog

    var name =""
    var phone =""
    var email=""
    var from=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PasswordPinFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[PasswordPinViewModel::class.java]

        from = PasswordPinArgs.fromBundle(requireArguments()).from
        phone = PasswordPinArgs.fromBundle(requireArguments()).userphone

        if(from.equals("Register")){
            name = PasswordPinArgs.fromBundle(requireArguments()).username.toString()
            email = PasswordPinArgs.fromBundle(requireArguments()).useremail.toString()
        }


        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Validating")
        progressDialog.setMessage("In-Progress.Please Wait")

        getValidated()
        Log.e("DATAFROM", "$phone")
    }

    private fun getValidated() {
        _binding.btnSignin.setOnClickListener{
            if(_binding.firstPinView.text.toString().isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter OTP",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            progressDialog.show()
            if(from.equals("Register")){
                viewModel.getUserRegistered(name,
                    phone,
                    email,
                    _binding.firstPinView.text.toString())
                fetchData()
            } else {
                viewModel.checkLogin(
                    phone,
                    _binding.firstPinView.text.toString())

                Log.e("DATAFROM", "$phone")
                Log.e("DATAFROM12333", _binding.firstPinView.text.toString())

                fetchLoginData()
            }
        }
    }

    private fun fetchData() {
        viewModel.response.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Log.e("response", response.data.Message)
                        if(response.data.status==1) {
                            viewModel.saveLoginStatus(true)
                            viewModel.saveUserID(response.data.data.UsersID)
                            viewModel.saveUserName(response.data.data.FullName)
                            findNavController().navigate(R.id.action_PasswordPinFragment_to_DashboardFragment)
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

    private fun fetchLoginData() {
        viewModel.responseLogin.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                       // Log.e("response", response.data.Message)
                        if(response.data.status==1) {
                            viewModel.saveLoginStatus(true)
                            viewModel.saveUserID(response.data.data.UsersID)
                            viewModel.saveUserName(response.data.data.FullName)
                            findNavController().navigate(R.id.action_PasswordPinFragment_to_DashboardFragment)
                        } else{
                            Toast.makeText(
                                requireContext(),
                                response.data.Message,
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