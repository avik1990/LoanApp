package com.app.loanserviceapp.verification.personal

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
import com.app.loanserviceapp.databinding.PersonalFragmentBinding
import com.app.loanserviceapp.register.RegisterDirections
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.readString
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.documentation.DocumenFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class PersonalFragment : Fragment() {

    companion object {
        fun newInstance() = PersonalFragment()
    }

    private lateinit var viewModel: PersonalViewModel
    private lateinit var _binding: PersonalFragmentBinding
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog

    var packageId=""
    var insuranceFees=""
    var processingFees=""
    var packagename=""
    var imageName=""

    var userName=""
    var mobileNo=""
    var age=""
    var currentAddress=""
    var permanenntAddress=""
    var emailAddress=""
    var userId:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[PersonalViewModel::class.java]


        runBlocking {
            val getUserName = context?.readString(Constants.USER_ID)
            userId = getUserName?.first().toString()
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading")
        progressDialog.setMessage("In-Progress.Please Wait")

        packageId = PersonalFragmentArgs.fromBundle(requireArguments()).packageid
        insuranceFees = PersonalFragmentArgs.fromBundle(requireArguments()).insuranceFees
        processingFees = PersonalFragmentArgs.fromBundle(requireArguments()).processingFees
        packagename = PersonalFragmentArgs.fromBundle(requireArguments()).packageName
        imageName = PersonalFragmentArgs.fromBundle(requireArguments()).imageName

        getValidation()
    }

    private fun getValidation() {
        _binding.btnpersonalSubmit.setOnClickListener{
            //findNavController().navigate(R.id.action_PersonalFragment_to_nav_confirmed)
            userName=_binding.editName.text.toString()
            mobileNo=_binding.editMobile.text.toString()
            age=_binding.editage.text.toString()
            currentAddress=_binding.ediaddress.text.toString()
            permanenntAddress=_binding.edipaddress.text.toString()
            emailAddress=_binding.ediemail.text.toString()

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
            } else if(age.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Age",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }else if(currentAddress.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Current Address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }else if(permanenntAddress.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Permanent Address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }else if(emailAddress.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please Enter Email Address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            progressDialog.show()
            val list: List<String> = listOf(*imageName.split(",").toTypedArray())
            viewModel.getSubmittedApplication(userId,packageId,packagename,insuranceFees,processingFees,
                userName,mobileNo,age,currentAddress,permanenntAddress,emailAddress,list[0],list[1],list[2],list[3])
            fetchData()
        }
    }
    private fun fetchData() {
        viewModel.responsesubmit.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if(response.data.status==1) {
                            findNavController().navigate(R.id.action_PersonalFragment_to_nav_confirmed)
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