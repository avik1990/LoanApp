package com.app.loanserviceapp.dashboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.loanserviceapp.R
import com.app.loanserviceapp.dashboard.model.MyApplication
import com.app.loanserviceapp.loanapackages.LoanApprovalFragmentDirections
import com.app.loanserviceapp.utils.Constants
import kotlinx.android.synthetic.main.documen_fragment.view.*
import kotlinx.android.synthetic.main.row_loan_status.view.*

class LoanStatusAdpater(private val onNewsSelected: onRowItemSelected,val userList: List<MyApplication>) : RecyclerView.Adapter<LoanStatusAdpater.ViewHolder>() {

    private var context: Context? = null

    interface onRowItemSelected {
        fun getSelectedItem(id: String, catname: String)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_loan_status, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.itemView.btnpayment.setOnClickListener {
            onNewsSelected.getSelectedItem(userList[position].ApplicationID,userList[position].ProcessingFee)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: MyApplication) {
            itemView.packageName.text=user.PackageName
            itemView.applicationDate.text=user.ApplicationDate
            itemView.applicationStatus.text=user.ApplicationStatus
            Log.e("StatusValue",user.ApplicationStatus+"::"+Constants.PAYMENTSTUATS.PEND)
            if(user.ApplicationStatus == "APR"){
                Log.e("StatusValue11",user.ApplicationStatus+"::"+Constants.PAYMENTSTUATS.PEND)
                itemView.btnpayment.visibility=View.VISIBLE
            }else{
                Log.e("StatusValue22",user.ApplicationStatus+"::"+Constants.PAYMENTSTUATS.PEND)
                itemView.btnpayment.visibility=View.GONE
            }
        }
    }

}