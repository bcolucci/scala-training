package ratelim.hotels

import ratelim.utils.SortOrder

import scala.collection.SortedSet

trait HotelRepository {

  def Search(cityId: String): Set[Hotel]

  def Search(cityId: String, sort: SortOrder.Value): SortedSet[Hotel]

  def orderByPrice(sort: SortOrder.Value): Ordering[Hotel] = (h1: Hotel, h2: Hotel) => {
    val diff = h1.Price - h2.Price
    sort match {
      case SortOrder.ASC => if (diff < 0) -1 else +1
      case SortOrder.DESC => if (diff > 0) -1 else +1
    }
  }

}

