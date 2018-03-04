package ratelim.hotels

import akka.actor.{ Actor, ActorLogging, Props }
import ratelim.hotels.HotelRegistryActor.Search
import ratelim.utils.SortOrder

object HotelRegistryActor {

  def props: Props = Props[HotelRegistryActor]

  final case class Search(cityId: String, sort: Option[String])

}

class HotelRegistryActor extends Actor with ActorLogging {

  var repository = HotelInMemRepository("hoteldb.csv")

  override def receive: Receive = {
    case Search(cityId, sort) =>
      val hotels = sort match {
        case None => repository.Search(cityId)
        case _ => repository.Search(cityId, SortOrder.withName(sort.get.toUpperCase()))
      }
      sender() ! Hotels(hotels.toSeq)
  }
}