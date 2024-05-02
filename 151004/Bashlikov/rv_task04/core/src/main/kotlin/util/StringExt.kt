package util

fun String.inRange(from: Int, to: Int): Boolean {
    return this.length in from..to
}