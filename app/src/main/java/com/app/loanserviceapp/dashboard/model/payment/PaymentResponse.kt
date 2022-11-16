package com.app.loanserviceapp.dashboard.model.payment

data class PaymentResponse(
    val Message: String,
    val `data`: Data,
    val status: Int
)