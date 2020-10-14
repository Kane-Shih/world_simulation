package world

class ExperimentReporter(
    private val experimentName: String
) {
    private var rankingSum = 0
    private var rankMin = -1
    private var rankMax = -1
    private var moneySum = 0
    private var moneyMin = Int.MIN_VALUE
    private var moneyMax = -1

    fun updateRecord(ranking: Int, money: Int) {
        rankingSum += ranking
        if (rankMax == -1 || ranking > rankMax) {
            rankMax = ranking
        }
        if (rankMin == -1 || ranking < rankMin) {
            rankMin = ranking
        }

        moneySum += money
        if (moneyMax == Int.MIN_VALUE || money > moneyMax) {
            moneyMax = money
        }
        if (moneyMin == Int.MIN_VALUE || money < moneyMin) {
            moneyMin = money
        }
    }

    fun printResult(sampleSize: Int) {
        println("=== Experiment [$experimentName] Result ===")
        println("  Rank - AVG:${rankingSum / sampleSize} | WORST:$rankMax | BEST:$rankMin")
        println("  Money - AVG:\$${moneySum / sampleSize} | MIN:\$$moneyMin | MAX:\$$moneyMax")
    }
}