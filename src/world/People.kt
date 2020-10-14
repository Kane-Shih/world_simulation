package world

class People(
    val name: String,
    var money: Int,
    var ability: Float, // 0~1
    var lucky: Float // 0~1
) {
    var matches = 0
    var wins = 0
    var losses = 0

    var tag: String? = null
}

fun createChosenOne(initMoney: Int) = People(
    name = "Chosen One",
    money = initMoney,
    ability = 1f,
    lucky = 1f
)

fun People.log(prefix: String? = null) {
    var s = "$name has \$$money." +
            " (ability:$ability, lucky:$lucky)" +
            " [winning ratio:${wins.toFloat() / matches.toFloat()}]"
    if (tag != null) {
        s += " tag:$tag"
    }
    if (prefix != null) {
        s = prefix + s
    }
    Logger.log(s)
}