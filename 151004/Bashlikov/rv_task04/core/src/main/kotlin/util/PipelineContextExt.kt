package util

suspend fun respond(
    isCorrect: () -> Boolean,
    onCorrect: suspend () -> Unit,
    onIncorrect: suspend () -> Unit
) {
    if (isCorrect()) {
        onCorrect()
    } else {
        onIncorrect()
    }
}

