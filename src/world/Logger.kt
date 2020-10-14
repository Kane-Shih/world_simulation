package world

object Logger {
    var isEnabled = false

    fun log(any: Any?) {
        if (isEnabled) println("$any")
    }
}