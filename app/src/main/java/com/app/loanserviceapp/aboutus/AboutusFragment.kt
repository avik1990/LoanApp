package com.app.loanserviceapp.aboutus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.loanserviceapp.R

class AboutusFragment : Fragment() {

    companion object {
        fun newInstance() = AboutusFragment()
    }

    private lateinit var viewModel: AboutusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.aboutus_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AboutusViewModel::class.java]
        // TODO: Use the ViewModel
    }

}