package dev.akif.kodluyoruz.flist

object FListDemo {
  def main(args: Array[String]): Unit = {
    val list = (1 to 100 by 2).toList

    val flist1 = FList(1, 2, 3)

    val flist2 = FList(list)

    val flist3 = FList("hello", "world")
  }
}
