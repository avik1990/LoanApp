package com.app.loanserviceapp.payment

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.app.loanserviceapp.R
import com.app.loanserviceapp.register.RegisterDirections
import com.app.loanserviceapp.utils.Constants
import com.app.loanserviceapp.utils.Datastore.Clear
import com.app.loanserviceapp.utils.Datastore.readBool
import com.app.loanserviceapp.utils.Datastore.readString
import com.app.loanserviceapp.utils.HashGenerationUtils
import com.app.loanserviceapp.utils.NetworkResult
import com.app.loanserviceapp.verification.personal.PersonalFragmentArgs
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    var packageName: String = ""
    var processingFees: String = ""
    private var userId: String = ""

    var userphone: String = ""
    var userName: String = ""
    var userEmail: String = ""
    private lateinit var progressDialog: ProgressDialog


    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]

        packageName = PaymentFragmentArgs.fromBundle(requireArguments()).packageName
        processingFees = PaymentFragmentArgs.fromBundle(requireArguments()).processingFees
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Validating Payment")
        progressDialog.setMessage("In-Progress.Please Wait")

        CoroutineScope(Dispatchers.Main).launch {
            val getUserId = context?.readString(Constants.USER_ID)
            userId = getUserId?.first().toString()

            val getUserName = context?.readString(Constants.USER_NAME)
            userName = getUserName?.first().toString()

            val getUserPhone = context?.readString(Constants.USER_PHONE)
            userphone = getUserPhone?.first().toString()

            val getUserEmail = context?.readString(Constants.USER_EMAIL)
            userEmail = getUserEmail?.first().toString()
        }

        etProcessingFees.text = "\u20B9 " + processingFees

        btnPayment.setOnClickListener {
            paymentFunc(processingFees)
        }
    }

    private fun paymentFunc(processingFees: String) {
        var transactionId = System.currentTimeMillis().toString() + "##" + userId
        val payUPaymentParams =
            PayUPaymentParams.Builder().setAmount(processingFees).setIsProduction(true)
                .setKey(Constants.testKey).setProductInfo(packageName).setPhone(userphone)
                .setTransactionId(transactionId).setFirstName(userName).setEmail(userEmail)
                .setSurl(Constants.surl).setFurl(Constants.furl).setUserCredential(userId).build()

        PayUCheckoutPro.open(requireActivity(),
            payUPaymentParams,
            object : PayUCheckoutProListener {

                override fun onPaymentSuccess(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    Log.e("onPaymentSuccess", payUResponse.toString())
                    sentStatusT0server(payUResponse)
                }

                override fun onPaymentFailure(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    Log.e("onPaymentFailure", payUResponse.toString() + "----" + merchantResponse)

                    sentStatusT0server(payUResponse)
                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    //sentStatusT0server("")
                }


                override fun onError(errorResponse: ErrorResponse) {
                    Log.e("pAYMENTeRROR", errorResponse.toString())
                    val errorMessage: String
                    if (errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty()) errorMessage =
                        errorResponse.errorMessage!!
                    else {
                        //errorMessage = resources.getString(R.string.some_error_occurred)
                        errorMessage = "ERROR RECIEVED PAYMENT"
                        Log.e("pAYMENTeRROR1", errorResponse.toString())
                    }
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                }

                override fun generateHash(
                    valueMap: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    if (valueMap.containsKey(CP_HASH_STRING) && valueMap.containsKey(CP_HASH_STRING) != null && valueMap.containsKey(
                            CP_HASH_NAME
                        ) && valueMap.containsKey(CP_HASH_NAME) != null
                    ) {

                        val hashData = valueMap[CP_HASH_STRING]
                        val hashName = valueMap[CP_HASH_NAME]

                        //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                        val hash: String? =
                            HashGenerationUtils.generateHashFromSDK(hashData!!, Constants.testSalt)
                        if (!TextUtils.isEmpty(hash)) {
                            val dataMap: HashMap<String, String?> = HashMap()
                            dataMap[hashName!!] = hash!!
                            hashGenerationListener.onHashGenerated(dataMap)
                        }
                    }
                }
            })
    }

    private fun sentStatusT0server(payUResponse: Any?) {
        progressDialog.show()
        viewModel.sentPaymentStatusResponse(payUResponse.toString())
        fetchData()
    }

    private fun fetchData() {
        viewModel.response.observe(requireActivity()) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        if (response.data.status == 1) {
                            findNavController().navigate(R.id.action_Payment_to_DashboardFragment)
                        } else {
                            Toast.makeText(
                                requireContext(), response.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    progressDialog.dismiss()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(), response.message, Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }
}