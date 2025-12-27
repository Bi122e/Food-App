package com.example.foodapp.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//"hom nay, 15:30, hom qua, 20:15, "15/12/1233. 10:00"
fun Date.formatToDateTime(): String {
    val now = Date()
    val diff = now.time - this.time
    val dayInMillis = 24 * 60 * 60 * 1000

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return when {
        diff < dayInMillis && isSameDay(this, now) ->
            "Hôm nay, ${timeFormat.format(this)}"
        diff < 2 * dayInMillis  && isYesterday(this, now) ->
            "Hôm qua, ${timeFormat.format(this)}"
        diff < 7 * dayInMillis ->
            "${getDayOfWeek(this)}, ${timeFormat.format(this)}"
        else ->
            "${dateFormat.format(this)}, ${timeFormat.format(this)}"
    }
}

//format 14/12/2021
fun Date.formatToShortDate(): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(this)
}

//format 15:30
fun Date.formatToTime(): String {
    val format = SimpleDateFormat("HH::mm", Locale.getDefault())
    return format.format(this)
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return format.format(date1) == format.format(date2)
}
private fun isYesterday(date: Date, now: Date): Boolean {
    val yesterday = Date(now.time - 24 * 60 * 60 * 1000)
    return isSameDay(date, yesterday)
}
private fun getDayOfWeek(date: Date): String {
    val format = SimpleDateFormat("EEEE", Locale("vi", "VN"))
    return format.format(date)
}
//vnd 500000 -> 50.000d

fun Int.toVND(): String {
    return "%,d".format(this).replace(",", ".")
}

fun Double.toVND(): String = this.toInt().toVND()

//format 5000 -> 50.000d

fun Int.toFormattedPrice(): String = "${this.toVND()}d"

fun Double.toFormattedPrice(): String = "${this.toVND()}d"

//validate email
fun String.isValidEmail(): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return this.matches(regex)
}
//validate VN phone
fun String.isValidVietnamesePhone(): Boolean {
    val regex = Regex("^(0[3|5|7|8|9])+([0-9]{8})$")
    return this.matches(regex)
}

//format phone: 0901212 -> 090 909 123
fun String.formatPhone(): String {
    if (this.length != 10 ) return this
    return "${substring(0, 4)} ${substring(4,7)} ${substring(7)}"
}

//truncate
fun String.truncate(maxLength: Int): String {
    return if(length > maxLength) {
        "${take(maxLength -3)}..."
    } else this
}

//capitalize

fun String.capitalizeWords(): String {
    return split("").joinToString(" ") {word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }
}

//convert boolean

fun Boolean.toVietnameseText(
    trueText: String = "Co",
    falseText: String ="Khong"
): String {
    return if (this)trueText else falseText
}