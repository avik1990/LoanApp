package com.app.loanserviceapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.loanserviceapp.R
import com.app.loanserviceapp.dashboard.model.MyApplication
import kotlinx.android.synthetic.main.row_loan_status.view.*


class LoanStatusAdpater(val userList: List<MyApplication>) : RecyclerView.Adapter<LoanStatusAdpater.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanStatusAdpater.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_loan_status, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: LoanStatusAdpater.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: MyApplication) {
            itemView.appId.text="ID: #00000"+user.ApplicationID
            itemView.packageName.text="Package: "+user.PackageName
            itemView.insuranceFee.text="Ins Fees "+user.InsuranceFee
            itemView.processingFee.text="Process. Fees: "+user.ProcessingFee
            itemView.completionStatus.text="Comp. Status: "+user.IsCompleteApplication
            itemView.applicationDate.text="App. Date: "+user.ApplicationDate
            itemView.applicationStatus.text="Loan Status: "+user.ApplicationStatus
        }
    }

}