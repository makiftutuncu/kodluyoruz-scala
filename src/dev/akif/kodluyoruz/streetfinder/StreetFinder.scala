package dev.akif.kodluyoruz.streetfinder

trait StreetFinder {
  def findStreets(streets: List[String], names: Set[String]): List[String]
}


