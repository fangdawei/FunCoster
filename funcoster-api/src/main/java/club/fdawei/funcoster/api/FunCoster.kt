package club.fdawei.funcoster.api

import android.util.Log

/**
 * Created by david on 2019/06/12.
 */
object FunCoster {

    private val funMap = mutableMapOf<String, CostData>()

    @JvmField
    var threshold = 500 * 1000 * 1000L //500ms

    @JvmStatic
    fun onFunCall(name: String, cost: Long) {
        val costData = funMap[name] ?: CostData(name).apply { funMap[name] = this }
        costData.addCost(cost)
        if (cost > threshold) {
            Log.e("FunCoster", "fun execution cost too much time, $costData")
        }
    }

    @JvmStatic
    fun print() {
        funMap.values.sortedByDescending { it.average }.forEach {
            Log.i("FunCoster", "" + it)
        }
    }
}