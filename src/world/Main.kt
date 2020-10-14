@file:Suppress("SENSELESS_COMPARISON", "ConstantConditionIf")

package world

import java.io.File
import world.Logger.log

const val hasOutputCsvFile = true
const val hasExperiment = true
const val experimentSampleSize = 4

private fun setup(): World.Config {
    Logger.isEnabled = false

    val matchRounds = 50000
    val config = World.Config(
        peopleCount = 10000,
        peopleInitialMoney = 1000,
        matchRounds = matchRounds,
        hasChosenOne = false, // create one whose ability and lucky are both 1.0f
        singleBet = 50,
        canBankruptcy = true,
        hasLuckyDifference = true,   // lucky:   winning chance
        hasAbilityDifference = true, // ability: taking more/less money when wins
        experiment = if (hasExperiment) getExperiment(
            matchRounds / 2, // when to apply change
            "exp",
            increaseLucky = true,
            increaseAbility = true
        ) else null,
        sortResult = true
    )
    log("config: $config")
    return config
}

fun main() {
    val config = setup()
    val world = World()
    if (config.experiment == null) {
        world.simulate(config).apply {
            log("=== No experiment, simulation DONE ===")
            this.last().log("1st: ")
            this[this.size / 2].log("middle(${size / 2}): ")
            this.first().log("last: ")
            if (hasOutputCsvFile) {
                createReportFile(config.getFileName().also {
                    log("Output: $it")
                })
            }
        }
    } else {
        val reporter = ExperimentReporter(config.experiment.name)
        repeat(experimentSampleSize) { times ->
            world.simulate(config).apply {
                if (hasOutputCsvFile) {
                    createReportFile(config.getFileName())
                }

                val target = find { it.tag == config.experiment.tag }!!
                val ranking = this.size - this.indexOf(target) // `this` is already sorted
                reporter.updateRecord(ranking, target.money)
                target.log("> Times($times) result ranking $ranking/${this.size} - ")
            }
        }
        reporter.printResult(experimentSampleSize)
    }
}

private fun getExperiment(
    expActionRound: Int,
    experimentTag: String,
    increaseLucky: Boolean,
    increaseAbility: Boolean
): Experiment? {
    return if (increaseLucky && increaseAbility) {
        IncreaseBothAbilityAndLuckyExp(expActionRound, experimentTag)
    } else if (increaseAbility) {
        IncreaseAbilityExp(expActionRound, experimentTag)
    } else if (increaseLucky) {
        IncreaseLuckyExp(expActionRound, experimentTag)
    } else {
        DoNothingExp(expActionRound, experimentTag)
    }
}

private fun List<People>.createReportFile(fileName: String): File {
    val result = StringBuilder()
    result.append("name,money,matches,wins,losses,ability,lucky,tag\n")
    this.forEach {
        result.append(
            it.name + "," + it.money
                    + "," + it.matches + "," + it.wins + "," + it.losses
                    + "," + it.ability + "," + it.lucky
                    + "," + if (it.tag != null) it.tag else "" +
                    "\n"
        )
    }
    return File(fileName).apply { writeText(result.toString()) }
}

fun World.Config.getFileName(): String {
    val folder = "world_simulation/".also { File(it).mkdir() }
    val name = "World[" +
            "p:$peopleCount,m:$peopleInitialMoney,r:$matchRounds,b:$canBankruptcy," +
            "LD:$hasLuckyDifference,AD:$hasAbilityDifference,theOne:$hasChosenOne]"
    val nameSuffix = "-${System.currentTimeMillis()}.csv"
    return if (experiment == null) {
        folder + name + nameSuffix
    } else {
        folder + name + "-exp:${experiment.name}" + nameSuffix
    }
}