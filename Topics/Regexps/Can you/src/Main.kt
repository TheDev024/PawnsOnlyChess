fun main() {
    val answer = readln()
    // write your code here
    val regex = Regex("I can('t)? do my homework on time!")
    println(regex.matches(answer))
}