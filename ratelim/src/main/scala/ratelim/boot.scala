package ratelim

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ RejectionHandler, Route }
import akka.stream.ActorMaterializer
import ratelim.hotels.{ HotelRegistryActor, HotelRoutes }
import ratelim.utils.JsonSupport

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object boot extends App with JsonSupport with HotelRoutes {

  lazy val routes: Route = hotelRoutes
  implicit val system: ActorSystem = ActorSystem("ratelimServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val hotelRegistryActor: ActorRef = system.actorOf(HotelRegistryActor.props, "hotelRegistryActor")

  implicit def rejectionHandler = RejectionHandler.newBuilder().handle {
    case RateLimitRejection => complete(TooManyRequests, "Rate limit exceeded")
  }.handleNotFound(complete(NotFound, "Resource not found"))
    .result()

  private val port: Int = 8080

  Http().bindAndHandle(routes, "localhost", port)
  println(s"server listening on :$port")

  Await.result(system.whenTerminated, Duration.Inf)

}
