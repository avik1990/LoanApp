package com.app.loanserviceapp.login

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.databinding.LoginFragmentBinding
import com.app.loanserviceapp.register.RegisterDirections
import com.app.loanserviceapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : Fragment() {

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var _binding: LoginFragmentBinding
    private val binding get() = _binding!!
    var userNPhone: String = ""
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        _binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_Login_to_Register)
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Validating")
        progressDialog.setMessage("In-Progress.Please Wait")

        getValidated()
    }

    private fun getValidated() {
        _binding.btnLogin.setOnClickListener {
            userNPhone = _binding.editText.text.toString()
            if (userNPhone.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Enter Mobile",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            progressDialog.show()
            viewModel.checkLogin(userNPhone)

            fetchData()
        }
    }

    private fun fetchData() {
        viewModel.responseOTP.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Log.e("response", response.data.Message)
                        if (response.data.status == 1) {
                            val action = LoginDirections.actionLoginToPasswordPinFragment(
                                "", userNPhone, "",
                                "Login"
                            )
                            findNavController().navigate(action)
                            //findNavController().navigate(R.id.action_Login_to_PasswordPinFragment)
                        } else {
                            findNavController().navigate(R.id.action_Login_to_Register)
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