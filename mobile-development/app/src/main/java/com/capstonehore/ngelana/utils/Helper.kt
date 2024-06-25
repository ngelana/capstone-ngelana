package com.capstonehore.ngelana.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.withDateFormat(): String {
    val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    val date = inputFormat.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
}

fun String.dateFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    val date = inputFormat.parse(this) as Date
    return outputFormat.format(date)
}

fun String?.splitAndReplaceCommas(): List<String> {
    return this?.split(", ")?.map { it.replace("_", " ") } ?: emptyList()
}

fun String.capitalizeEachWord(): String {
    return split(" ").joinToString(" ") { it1 ->
        it1.replaceFirstChar { it2 ->
            if (it2.isLowerCase()) it2.titlecase(Locale.getDefault()) else it2.toString()
        }
    }
}