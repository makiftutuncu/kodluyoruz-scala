package dev.akif.kodluyoruz.streetfinder

trait StreetFinder {
  def findStreets(names: Set[String]): List[String]
}


