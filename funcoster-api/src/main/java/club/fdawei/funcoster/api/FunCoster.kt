package club.fdawei.funcoster.api

import android.os.Looper
import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by david on 2019/06/12.
 */
object FunCoster {

    private val funMap: MutableMap<String, CostData> = ConcurrentHashMap()

    @JvmField
    var threshold = 500 * 1000 * 1000L //500ms

    @JvmField
    var noticeOnlyMain = true

    @JvmStatic
    fun onFunCall(name: String, cost: Long) {
        val costData = getCostData(name)
        costData.addCost(cost)
        if (ifNeedNotice(cost)) {
            Log.e("FunCoster", "Method(${costData.key}) is too time-consuming, cost ${ns2ms(cost)}ms")
        }
    }

    @JvmStatic
    fun ifNeedNotice(cost: Long): Boolean {
        return if (cost > threshold) {
            if (noticeOnlyMain) {
                isMainThread()
            } else {
                true
            }
        } else {
            false
        }
    }

    @JvmStatic
    fun print() {
        funMap.values.sortedByDescending { it.average }.forEach {
            Log.i("FunCoster", "" + it)
        }
    }

    @JvmStatic
    private fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

    @JvmStatic
    private fun getCostData(key: String): CostData {
        return funMap[key] ?: synchronized(funMap) {
            funMap[key] ?: CostData(key).apply { funMap[key] = this }
        }
    }
}