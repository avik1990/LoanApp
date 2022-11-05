package com.app.loanserviceapp.verification.documentation

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Mms.Part
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.databinding.DocumenFragmentBinding
import com.app.loanserviceapp.password.PasswordPinArgs
import com.app.loanserviceapp.register.RegisterDirections
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.utils.fileutil.FileUtil
import com.app.loanserviceapp.utils.imageutils.CustomImagePicker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class DocumenFragment : Fragment() {

    companion object {
        fun newInstance() = DocumenFragment()
    }

    private lateinit var customImagePicker: CustomImagePicker

    private lateinit var viewModel: DocumenViewModel
    private lateinit var _binding: DocumenFragmentBinding
    private val binding get() = _binding!!
    private var selFlag: Int? = 0
    val listFileUris = arrayListOf<String>()
    val partsList = arrayListOf<MultipartBody.Part>()
    val uploadedFilename = arrayListOf<String>()
    private lateinit var progressDialog: ProgressDialog
    var count = 0
    var packageId = ""
    var insuranceFees = ""
    var processingFees = ""
    var packagename = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadedFilename.clear()
        listFileUris.clear()
        listFileUris.add("")
        listFileUris.add("")
        listFileUris.add("")
        listFileUris.add("")

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading")
        progressDialog.setMessage("In-Progress.Please Wait")


        customImagePicker = CustomImagePicker(this)
        var random = kotlin.math.abs((0..999999999999).random())

        var fileName = "$random.jpg"

        customImagePicker.registerListener(
            folder = FileUtil.CHATTER_NOTE_THUMBNIL,
            fileName = fileName, height = 300, width = 400, isCompressed = true
        ) { _, fileUri ->
            when (selFlag) {
                1 -> {
                    listFileUris[0] = fileUri
                    putImageBitmap(_binding.imgPhoto, fileUri)
                }
                2 -> {
                    listFileUris[1] = fileUri
                    putImageBitmap(_binding.imgAdhar, fileUri)
                }
                3 -> {
                    listFileUris[2] = fileUri
                    putImageBitmap(_binding.imgPan, fileUri)
                }
                4 -> {
                    listFileUris[3] = fileUri
                    putImageBitmap(_binding.imgIncomeProof, fileUri)
                }
            }
        }

        _binding = DocumenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DocumenViewModel::class.java]
        count = 0

        packageId = DocumenFragmentArgs.fromBundle(requireArguments()).packageId
        insuranceFees = DocumenFragmentArgs.fromBundle(requireArguments()).insuranceFees
        processingFees = DocumenFragmentArgs.fromBundle(requireArguments()).processingFees
        packagename = DocumenFragmentArgs.fromBundle(requireArguments()).packageName

        _binding.camPassport.setOnClickListener {
            selFlag = 1
            customImagePicker.pickImage()
        }
        _binding.camAdhaar.setOnClickListener {
            selFlag = 2
            customImagePicker.pickImage()
        }
        _binding.camPan.setOnClickListener {
            selFlag = 3
            customImagePicker.pickImage()
        }
        _binding.camIncomeProof.setOnClickListener {
            selFlag = 4
            customImagePicker.pickImage()
        }
        _binding.btnLoanApplyLoan.setOnClickListener {
            if (listFileUris.contains("")) {
                Toast.makeText(context, "Select Document to Upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                progressDialog.show()
                for (i in 0 until listFileUris.size) {
                    partsList.add(preparefilepart(listFileUris[i].toUri()))
                    viewModel.uploadFile(partsList[i])
                }
                fetchData()
            }
           /* val action = DocumenFragmentDirections.actionDocumenFragmentToPersonalFragment(
                "1",
                "1",
                "insuranceFees",
                "processingFees",
                uploadedFilename.toString()
            )
            findNavController().navigate(action)*/
        }
    }

    private fun fetchData() {
        viewModel.responseOTP.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if (response.data.status == 1) {
                            count++
                            uploadedFilename.add(response.data.data.UploadedFileName[0].AttachemnetFile)
                            if (count == 4) {
                                val action =
                                    DocumenFragmentDirections.actionDocumenFragmentToPersonalFragment(
                                        packagename,
                                        packageId,
                                        insuranceFees,
                                        processingFees,
                                        uploadedFilename.toString()
                                    )
                                findNavController().navigate(action)
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
                    //Log.e("response", response.message.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        customImagePicker.onActivityResult(requestCode, resultCode, data)
    }

    private fun putImageBitmap(view: ImageView?, bitmap: String) {
        view?.setImageURI(bitmap.toUri())
    }

    private fun preparefilepart(uri: Uri): MultipartBody.Part {
        val bin = File(uri.toString())
        val reqBody: RequestBody = bin.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        return createFormData("PostFileName[]", bin.name, reqBody)
    }

}