package club.fdawei.funcoster.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import club.fdawei.funcoster.api.FunCoster
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var index = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello.setOnClickListener {
            FunCoster.print()
        }
    }

    override fun onResume() {
        super.onResume()

        tvHello.postDelayed({
            test1()
            test2()
            test3()
        }, 1000)
    }

    fun test1(): Long {
        return (index + 2000L)
    }

    fun test2() {
        var value = 0
        for (i in 1..100000) {
            value += i
        }
    }

    fun test3() {
        Thread.sleep(1500)
    }
}
