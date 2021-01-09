package dev.akif.kodluyoruz.streetfinder

import java.io.File

trait CsvLoader {
  def loadCsv(file: File): List[String]
}


