package cn.gus.fg

import java.io.{File, PrintWriter}

import cn.gus.fg.CommbRule._

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import util.control.Breaks._

object FineGrained {


  def main(args: Array[String]): Unit = {
    val writer = new PrintWriter(new File("/Users/gus97/Desktop/spring.txt"))
    var i = 0
    for (_ <- 1 to 100000) {
      breakable {
        i += 1
        val initPok = ArrayBuffer[Int]()

        for (x <- 3 to 15)
          initPok += (x, x, x, x)
        initPok += (99, 100)

        val sp = getSpringPok()

        if (sp.isEmpty) {
          break
        }
        val map = mutable.HashMap[Int, ArrayBuffer[Int]]()

        for (y <- 3 to 15)
          map += (y -> ArrayBuffer(1, 2, 3, 4))


        //println(map)

        val c1 = sp(0).map(x => {
          if (x <= 15) {
            if (map(x).isEmpty) break
            val s = Random.shuffle(map(x)).remove(0)
            map(x) -= s
            //println(map(x),s)
            x * 10 + s
          }
          else x
        })

        val c2 = sp(1).map(x => {
          if (x <= 15) {
            if (map(x).isEmpty) break
            val s = Random.shuffle(map(x)).remove(0)
            map(x) -= s
            //println(map(x),s)
            x * 10 + s
          }
          else x
        })

        val c3 = sp(2).map(x => {
          if (x <= 15) {
            if (map(x).isEmpty) break
            val s = Random.shuffle(map(x)).remove(0)
            map(x) -= s
            //println(map(x),s)
            x * 10 + s
          }
          else x
        })

        val c0 = sp(3).map(x => {
          if (x <= 15) {
            if (map(x).isEmpty) break
            val s = Random.shuffle(map(x)).remove(0)
            map(x) -= s
            //println(map(x),s)
            x * 10 + s
          }
          else x
        })

        //println("====================================================>>>>>>>")
        //comb(ArrayBuffer() ++ sp(0), initPok -- sp(0)).flatMap(_._2).foreach(println)
        //println("====================================================>>>>>>>")

        if (c1.sum + c2.sum + c3.sum + c0.sum == 5009 ) {
          writer.println((c1.sortWith(_ > _) + "$" + c2.sortWith(_ > _) + "$" + c3.sortWith(_ > _) + "$" + c0.sortWith(_ > _)).replace("ArrayBuffer(", "").replace(")", "").replace(" ", ""))
        }else{
          println(c1)
          println(c2)
          println(c3)
          println(c0)
        }

        if (i % 1000 == 0) {
          println(i)
          writer.flush()
        }
        //      println(sp(0))
        //      println(sp(1))
        //      println(sp(2))
        //      println(sp(3))

      }
    }
    writer.close()
    //      println(sp.flatten.sortWith(_<_).map((_, 1))
    //        .groupBy(_._1)
    //        .map { x => (x._1, x._2.size)})
    //      println("====================================================>>>>>>>")
  }

  def countPL(ab: ArrayBuffer[Int], jpq: ArrayBuffer[Int]): Int = {
    val zp = comb(ab, jpq)
    val ai = new AI
    ai.opt = zp
    ai.jpq = jpq
    var num = 0
    num += 6 * ab.count(_ == 99)
    num += 10 * ab.count(_ == 100)
    num += 5 * ab.count(_ == 15)
    num += getCNum(ai)
    num -= getNCNum(ai)
    num
  }

  def getSpringPok(): ArrayBuffer[mutable.Buffer[Int]] = {

    var i = 0

    while (i < 100) {
      breakable {
        val initPok = ArrayBuffer[Int]()

        for (x <- 3 to 15)
          initPok += (x, x, x, x)
        initPok += (99, 100)

        val jpq = initPok.clone

        val pok = initPok.clone

        val sp1 = new Random().shuffle(initPok).take(17)

        val jpq1 = jpq -- sp1

        initPok --= sp1


        val sp2 = new Random().shuffle(initPok).take(17)

        val jpq2 = jpq -- sp2

        initPok --= sp2


        val sp3 = new Random().shuffle(initPok).take(17)

        val jpq3 = jpq -- sp3

        initPok --= sp3


        val three = initPok.clone

        //println("Threee=======> " + three)

        //println("sp1=======> " + sp1.sortWith(_ < _))

        //println("sp2=======> " + sp2.sortWith(_ < _))

        //println("sp3=======> " + sp3.sortWith(_ < _))

        //println("====================================================")
        val zp1 = comb(sp1, jpq1)
        val n1 = countPL(sp1, jpq1)
        //zp1.flatMap(_._2).foreach(println)
        //println(n1)
        //println("====================================================")
        val zp2 = comb(sp2, jpq2)
        val n2 = countPL(sp2, jpq2)
        //zp2.flatMap(_._2).foreach(println)
        //println(n2)
        //println("====================================================")
        val zp3 = comb(sp3, jpq3)
        val n3 = countPL(sp3, jpq3)
        //zp3.flatMap(_._2).foreach(println)
        //println(n3)
        //println("====================================================")


        val map = TreeMap(n1 -> zp1, n2 -> zp2, n3 -> zp3)

        val bestHandPok = map(map.lastKey).flatMap(_._2).flatten.toBuffer.sortWith(_ < _)

        val bad1 = map(map.firstKey).flatMap(_._2).flatten.toBuffer.sortWith(_ < _)

        val bad2 = (pok -- bestHandPok -- bad1 -- three).sortWith(_ < _)


        //println("====================================================")
        val up1 = new Random().shuffle(bad1).take(2)
        bad1 --= up1
        val up2 = new Random().shuffle(bad2).take(2)
        bad2 --= up2
        bestHandPok ++= up1 ++ up2

        val p21 = comb(ArrayBuffer() ++ bestHandPok, jpq -- bestHandPok)

        //p21.flatMap(_._2).foreach(println)

        /**
          * 扔牌逻辑：
          * 小于A的单（S1）+三带1的尾牌（S2）+ 长顺子可拆的尾牌（S3） 总和 = T
          * 小于A的对子(D1)+三带2的对子（D） 总和 = D
          * *
          * 1、若T>=4 优先扔 S1>S2>S3
          *
          * 2、若T+D>=4
          * 2.1若T<=1 扔2对子 优先扔 D1>D2 若T==2||3 扔 1单+1对
          *
          * 3、若T+D<4
          * 若存在三带1，给出最小的三带
          *
          * 4、否则跳出
          *
          * 5、如果连续100次不能迭代出结果，按最后1次发牌给三家
          *
          */

        val s1 = p21.getOrElse(11, ArrayBuffer()).filter(x => x.exists(_ < 15)).flatten

        val sd1 = p21.getOrElse(31, ArrayBuffer()).filter(x => x.last < 15)

        val sd2 = p21.getOrElse(32, ArrayBuffer()).filter(x => x.last < 15)

        val s2 = ArrayBuffer[Int]()

        val s3 = ArrayBuffer[Int]()

        val d2 = ArrayBuffer[Int]()

        for (x <- sd1) s2 += x.last

        for (x <- sd2) d2 += x.last

        val seq = p21.getOrElse(12345, ArrayBuffer()).filter(x => x.length > 5)

        for (x <- seq) s3 ++= x.slice(0, x.length - 5)

        val d1 = p21.getOrElse(2, ArrayBuffer()).filter(x => x.exists(_ < 15)).flatten

        //    println("====================================================")
        //    println(s1)
        //    println(s2)
        //    println(s3)
        //    println(d1)
        //    println(d2)
        //    if (sd1.nonEmpty) println(sd1.head)
        //    println("====================================================")
        //    println(three)
        //    println(bestHandPok)
        //    println(bad1)
        //    println(bad2)

        val four = ArrayBuffer[Int]()

        val t = s1.length + s2.length + s3.length
        val d = d1.length + d2.length

        if (t >= 4) {
          four ++= (s1 ++ s2 ++ s3).take(4)
        } else if (t + d >= 4) {
          if (t <= 1) {
            four ++= (d1 ++ d2).take(4)
          } else {
            four ++= (s1 ++ s2 ++ s3).take(2)
            four ++= (d1 ++ d2).take(2)
          }
        } else if (sd1.nonEmpty) {
          four ++= sd1.head
        }

        if (four.isEmpty) {
          i += 1
          break
        } else {
          return ArrayBuffer((ArrayBuffer[Int]() ++ bestHandPok -- four), bad1 ++ four.take(2), bad2 ++ four.takeRight(2), three)
        }
      }
    }
    return null
  }
}
