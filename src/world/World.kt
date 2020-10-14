package world

import kotlin.random.Random

class World {
    data class Config(
        val peopleCount: Int,
        val peopleInitialMoney: Int,
        val matchRounds: Int,
        val hasChosenOne: Boolean,
        val singleBet: Int,
        val canBankruptcy: Boolean,
        val hasLuckyDifference: Boolean,
        val hasAbilityDifference: Boolean,
        val experiment: Experiment?,
        val sortResult: Boolean = true
    )

    fun simulate(config: Config): List<People> {
        val peopleList = createPeopleList(
            peopleCount = config.peopleCount,
            peopleInitialMoney = config.peopleInitialMoney,
            hasChosenOne = config.hasChosenOne
        )
        val match = Match(
            bet = config.singleBet,
            canBankruptcy = config.canBankruptcy,
            useLucky = config.hasLuckyDifference,
            useAbility = config.hasAbilityDifference
        )

        if (config.experiment == null) {
            repeat(config.matchRounds) {
                peopleList.applyToRandomPair(action = match::match)
            }
        } else {
            if (config.experiment.actionRound >= config.matchRounds)
                throw IllegalArgumentException("Wrong experiment setup")

            repeat(config.experiment.actionRound) {
                peopleList.applyToRandomPair(action = match::match)
            }
            config.experiment.action(peopleList)
            repeat(config.matchRounds - config.experiment.actionRound) {
                peopleList.applyToRandomPair(action = match::match)
            }
        }

        return if (config.sortResult)
            peopleList.sortedBy { it.money }
        else
            peopleList
    }

    private fun createPeopleList(
        peopleCount: Int,
        hasChosenOne: Boolean,
        peopleInitialMoney: Int
    ) = List(peopleCount) {
        if (it == 0 && hasChosenOne) {
            createChosenOne(peopleInitialMoney)
        } else {
            People(
                name = "P$it",
                money = peopleInitialMoney,
                ability = Random.nextFloat().coerceAtLeast(0.1f),
                lucky = Random.nextFloat().coerceAtLeast(0.1f)
            )
        }
    }
}

fun <T> Collection<T>.applyToRandomPair(
    rule: (T, T) -> Boolean = { p1, p2 ->
        p1 !== p2
    },
    action: (T, T) -> Unit
) {
    var p1: T? = null
    var p2: T? = null

    while (p1 == null || p2 == null || !rule(p1, p2)) {
        p1 = this.random()
        p2 = this.random()
    }
    action(p1, p2)
}
