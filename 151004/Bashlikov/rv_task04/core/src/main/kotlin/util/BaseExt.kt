package util

suspend fun <T>getWithCheck(creator: suspend () -> T): T? {
    return try {
        creator()
    } catch (e: Exception) {
        null
    }
}