@file:Suppress("unused")

package world

import world.Logger.log

sealed class Experiment(
    val name: String,
    val actionRound: Int,
    val tag: String,
    val action: (List<People>) -> Unit
)

class IncreaseLuckyExp(actionRound: Int, tag: String) : Experiment(
    name = "IncreaseLuckyAf$actionRound",
    actionRound = actionRound,
    tag = tag,
    action = { peopleList ->
        val poorest = peopleList.randomPickFromPoorest()
        poorest.tag = tag
        poorest.log("Experiment: adjust lucky to 1.0f - ")
        poorest.lucky = 1.0f
    }
)

class IncreaseAbilityExp(actionRound: Int, tag: String) : Experiment(
    name = "IncreaseAbilityAt$actionRound",
    actionRound = actionRound,
    tag = tag,
    action = { peopleList ->
        val poorest = peopleList.randomPickFromPoorest()
        poorest.tag = tag
        poorest.log("Experiment: adjust ability to 1.0f - ")
        poorest.ability = 1.0f
    }
)

class IncreaseBothAbilityAndLuckyExp(actionRound: Int, tag: String) : Experiment(
    name = "IncreaseBothAbilityAndLuckyAt$actionRound",
    actionRound = actionRound,
    tag = tag,
    action = { peopleList ->
        val poorest = peopleList.randomPickFromPoorest()
        poorest.tag = tag
        poorest.log("Experiment: adjust ability & lucky to 1.0f - ")
        poorest.ability = 1.0f
        poorest.lucky = 1.0f
    }
)

class DoNothingExp(actionRound: Int, tag: String) : Experiment(
    name = "DoNothingAt$actionRound",
    actionRound = actionRound,
    tag = tag,
    action = { peopleList ->
        val poorest = peopleList.randomPickFromPoorest()
        poorest.tag = tag
        log("Experiment: do nothing to ${poorest.name}")
        poorest.log()
    }
)

private fun List<People>.randomPickFromPoorest(): People {
    val poorest = minBy { it.money }!!
    val minMoney = poorest.money
    return filter { it.money == minMoney }.toList().random()
}