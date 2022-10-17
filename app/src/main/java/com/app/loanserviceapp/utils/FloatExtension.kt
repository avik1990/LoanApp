package com.app.loanserviceapp.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Float.roundOffDecimalCellingTwoDecimal(): Float {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    val value = df.format(this)
    return value.getParsedFloatValue()
}
fun Float.roundOffDecimalFloorTwoDecimal(): Float {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    val value = df.format(this)
    return value.getParsedFloatValue()
}

fun Float.convertFloatToPercentageString(): String {
    return (this * 100).toInt().toString() + "%"
}

fun String.getParsedFloatValue(): Float {
    return if (this.contains(".")) {
        NumberFormat.getNumberInstance().parse(this)?.toFloat() ?: 0f
    } else {
        val nf: Number? = NumberFormat.getNumberInstance(Locale.getDefault()).parse(this)
        nf?.toFloat() ?: 0f
    }
}
