package club.fdawei.funcoster.api

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min


/**
 * Created by david on 2019/06/12.
 */
class CostData(
    val key: String
) {
    var max: Long = 0
        private set

    var min: Long = 0
        private set

    var average: Long = 0
        private set

    var count: Int = 0
        private set

    fun addCost(cost: Long) {
        max = max(max, cost)
        min = if (count == 0) cost else min(min, cost)
        average = (average * count + cost) / (count + 1)
        count++
    }

    override fun toString(): String {
        return "$key, " +
                "max=${msValueStr(max)}ms, " +
                "min=${msValueStr(min)}ms, " +
                "average=${msValueStr(average)}ms, " +
                "count=$count"
    }

    private fun msValueStr(value: Long): String {
        return BigDecimal.valueOf(value).divide(
            BigDecimal.valueOf(1000000),
            3,
            RoundingMode.HALF_UP
        ).toString()
    }

}