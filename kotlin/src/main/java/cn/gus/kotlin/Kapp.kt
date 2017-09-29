package cn.gus.kotlin

import java.util.*
import kotlin.collections.ArrayList


object Kapp {

    @JvmStatic
    fun main(args: Array<String>) {

        generateSpringPok()
    }

    fun generateSpringPok() {
        //初始化54张牌
        val initPok = ArrayList<Int>()
        for (i in 3..15)
            for (j in 0..3)
                initPok.add(i)
        initPok.add(99)
        initPok.add(100)

        Collections.shuffle(initPok)


        val p1 = initPok.subList(0,17)
        val p2 = initPok.subList(17,17*2)
        val p3 = initPok.subList(17*2,17*3)
        val three = initPok.takeLast(3)
        println(p1)
        println(p2)
        println(p3)
        println(three)


    }
}