package ratelim.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ratelim.hotels.{ Hotel, Hotels }
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val hotelJsonFormat = jsonFormat4(Hotel)
  implicit val hotelsJsonFormat = jsonFormat1(Hotels)

}

