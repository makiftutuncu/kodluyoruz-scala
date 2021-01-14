package dev.akif.kodluyoruz.streetfinder

import java.io.File
import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * See CSV file at: https://github.com/makiftutuncu/kodluyoruz-scala/blob/main/data/streets.csv
 *
 * Original data: https://github.com/life/il-ilce-mahalle-sokak-cadde-sql
 */
object Application extends CsvLoader with StreetFinder {
  private val streetsFilePath: String = "data/streets.csv"

  def main(args: Array[String]): Unit = {
    val requestedStreets = Set("ATATÜRK", "İNÖNÜ")

    println(s"Looking for streets matching: ${requestedStreets.mkString("'", "', '", "'")}")

    val startTime = System.currentTimeMillis()

    val allStreets     = loadCsv(new File(streetsFilePath))
    val matchedStreets = findStreets(allStreets, requestedStreets)

    val elapsedTime = (System.currentTimeMillis() - startTime) / 1000

    println(
      s"""
         |Total: ${allStreets.size}
         |Matched: ${matchedStreets.size}
         |Took: $elapsedTime second(s)
         |""".stripMargin)

    matchedStreets foreach println
  }

  override def loadCsv(file: File): List[String] =
    Try(Source.fromFile(file)) match {
      case Failure(openError) =>
        fail(s"Cannot open file: $file", openError)
        List.empty[String]

      case Success(source) =>
        Try(source.getLines().drop(1).grouped(1024)) match {
          case Failure(readError) =>
            source.close()
            fail(s"Cannot read lines from file: $file", readError)
            List.empty[String]

          case Success(lineBatches) =>
            val streets = lineBatches.foldLeft(List.empty[String]) {
              case (list, batch) =>
                list ++ extractStreets(batch.toList)
            }
            source.close()
            streets
        }
    }

  override def findStreets(streets: List[String], names: Set[String]): List[String] = {
    val streetsToNames =
      streets.foldLeft(Map.empty[String, Set[String]]) {
        case (namesMap, street) =>
          val namesInStreet = street.split(" ").map(_.trim).filter(_.nonEmpty).toSet

          namesMap + (street -> namesInStreet)
      }

    val matchingStreets = streetsToNames.filter {
      case (_, namesInStreet) =>
        namesInStreet.intersect(names).nonEmpty
    }

    matchingStreets.keys.toList
  }

  def extractStreets(lines: List[String]): List[String] =
    lines.foldLeft(List.empty[String]) {
      case (streets, line) =>
        line match {
          case s"$_,'$street',$_" =>
            streets :+ street

          case _ =>
            println(s"Cannot extract street name from line: $line")
            streets
        }
    }

  private def fail(message: String, openError: Throwable): Unit = {
    println(message)
    openError.printStackTrace()
  }
}
