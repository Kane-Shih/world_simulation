package world

import kotlin.math.roundToInt
import kotlin.random.Random

class Match(
    val bet: Int,
    val canBankruptcy: Boolean,
    val useLucky: Boolean,
    val luckyWeight: Float = 0.1f,
    val useAbility: Boolean
) {

    fun match(people1: People, people2: People) {
        val chance = if (useLucky) {
            0.5f + (luckyWeight * (people1.lucky - 0.5f)) // 0.45 ~ 0.55 when luckyWeight is 0.1f
        } else {
            0.5f
        }

        val isP1Win = Random.nextFloat() < chance
        if (isP1Win) {
            exchange(people1, people2)
        } else {
            exchange(people2, people1)
        }
    }

    private fun exchange(winner: People, loser: People) {
        val award = if (useAbility) {
            (winner.ability * bet).roundToInt().coerceAtLeast(1)
        } else {
            bet
        }
        val exchange = if (canBankruptcy && loser.money <= award) {
            loser.money
        } else {
            award
        }

        winner.money += exchange
        loser.money -= exchange

        winner.matches++
        loser.matches++

        winner.wins++
        loser.losses++
    }
}