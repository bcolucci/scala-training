package ratelim.hotels

import ratelim.utils.SortOrder

import scala.collection.SortedSet
import scala.io.Source

final case class HotelInMemRepository(csvPath: String) extends HotelRepository {

  private var values = Map[String, Set[Hotel]]()

  load()

  def Search(cityId: String): Set[Hotel] = values get cityId match {
    case None => Set.empty[Hotel]
    case hotels => hotels.get
  }

  def Search(cityId: String, sort: SortOrder.Value): SortedSet[Hotel] =
    SortedSet(values(cityId).toSeq: _*)(HotelInMemRepository.super.orderByPrice(sort))

  // loads hotels from the CSV
  private def load(): Unit = {
    val contents: Iterator[String] = Source.fromResource(csvPath).getLines
    contents.next() // remove the header
    for (line <- contents) {
      val hotel = HotelFactory.BuildFromArray(line.split(","))
      val cityHotels = values get hotel.CityId
      values += (cityHotels match {
        case None => hotel.CityId -> Set[Hotel](hotel)
        case _ => hotel.CityId -> (cityHotels.get + hotel)
      })
    }
  }

}

