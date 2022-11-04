package com.app.loanserviceapp.dashboard.model

data class Data(
    val hasPendingLoan: String,
    val MyApplications: List<MyApplication>,
    val ProfileDetails: ProfileDetails
)