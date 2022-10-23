fun main() {
    val report = readLine()!!
    //write your code here.
    val regex = Regex("^[0-9] wrong answers?$")
    println(regex.matches(report))
}