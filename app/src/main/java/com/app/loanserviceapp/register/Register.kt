package com.app.loanserviceapp.register

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.databinding.RegisterFragmentBinding
import com.app.loanserviceapp.datasource.RemoteDataSource
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : Fragment() {

    companion object {
        fun newInstance() = Register()
    }

    private lateinit var viewModel: RegisterViewModel
    private lateinit var _binding: RegisterFragmentBinding
    private val binding get() = _binding!!
    private lateinit var progressDialog:ProgressDialog
    var userName:String=""
    var userNPhone:String=""
    var userNEmail:String=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        /*_binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_PasswordPinFragment2)
        }*/
        _binding.tvTerms.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_TermsFragment)
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Validating")
        progressDialog.setMessage("In-Progress.Please Wait")


        getValidated()
    }

    private fun getValidated() {
        _binding.btnLogin.setOnClickListener{
            userName=_binding.editName.text.toString()
            userNPhone=_binding.editMobile.text.toString()
            userNEmail=_binding.editEmail.text.toString()

            if(_binding.editName.text.toString().isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Name",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if(_binding.editMobile.text.toString().isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Mobile",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if(_binding.editEmail.text.toString().isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Email",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            progressDialog.show()
            viewModel.fetchRegisterResponse(_binding.editName.text.toString(),
                _binding.editMobile.text.toString(),
                _binding.editEmail.text.toString())
            // _binding.pbDog.visibility = View.VISIBLE
            fetchData()
        }
    }

    private fun fetchData() {
        viewModel.response.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if(response.data.status==1) {
                            viewModel.sendForOTP(_binding.editMobile.text.toString())
                            viewModel.responseOTP.observe(requireActivity()){ response1->
                                response1.data.let {
                                    val action= RegisterDirections.actionRegisterFragmentToPasswordPinFragment2(userName,userNPhone,userNEmail,"Register")
                                    findNavController().navigate(action)
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
                    progressDialog.dismiss()
                }

                is NetworkResult.Error -> {
                    Log.e("response", response.message.toString())
                    //_binding.pbDog.visibility = View.GONE
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