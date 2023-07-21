import java.time.LocalDate

fun printDate(date: String) {
    val ld = LocalDate.parse(date)
    println("${ld.dayOfWeek}, ${ld.month} ${ld.dayOfMonth}, ${ld.year}")
}

fun main() {
    val date = readln()
    printDate(date)
}