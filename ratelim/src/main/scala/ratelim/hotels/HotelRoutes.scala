package ratelim.hotels

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.pattern.ask
import akka.util.Timeout
import ratelim.hotels.HotelRegistryActor.Search
import ratelim.utils.JsonSupport
import ratelim.{ RateLimitRejection, RateLimiter }

import scala.concurrent.duration._

trait HotelRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val hotelRoutes: Route = {
    headerValueByName("key") { key =>
      if (!RateLimiter.IsAuthorized(key))
        reject(RateLimitRejection)
      else
        pathPrefix("hotels" / Segment) { cityId =>
          get {
            parameters("sort".?) { sort =>
              val hotels = (hotelRegistryActor ? Search(cityId, sort)).mapTo[Hotels]
              complete(hotels)
            }
          }
        }
    }
  }

  def hotelRegistryActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(3.seconds)

}
