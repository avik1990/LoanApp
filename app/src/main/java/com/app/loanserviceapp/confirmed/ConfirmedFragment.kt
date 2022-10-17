package com.app.loanserviceapp.confirmed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.loanserviceapp.R

class ConfirmedFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmedFragment()
    }

    private lateinit var viewModel: ConfirmedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirmed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}