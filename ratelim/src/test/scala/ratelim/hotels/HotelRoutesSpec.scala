package ratelim.hotels

import akka.actor.ActorRef
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }
import ratelim.{ RateLimitRejection, RateLimiter }

class HotelRoutesSpec extends WordSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest
    with HotelRoutes {

  lazy val routes = hotelRoutes

  override val hotelRegistryActor: ActorRef =
    system.actorOf(HotelRegistryActor.props, "hotelRegistry")

  "HotelRoutes" should {

    "return hotels (GET /hotels)" in {
      val request = HttpRequest(uri = "/hotels/Bangkok")
        .withHeaders(RawHeader("key", RateLimiter.TEST_KEY))
      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`application/json`
        entityAs[Hotels].Values().map(_.Price) shouldEqual List[Double](
          5300, 60, 900, 25000, 2000, 1000, 2400
        )
      }
    }

    "return hotels order by price ASC (GET /hotels?sort=asc)" in {
      val request = HttpRequest(uri = "/hotels/Bangkok?sort=asc")
        .withHeaders(RawHeader("key", RateLimiter.TEST_KEY))
      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`application/json`
        entityAs[Hotels].Values().map(_.Price) shouldEqual List[Double](
          60, 900, 1000, 2000, 2400, 5300, 25000
        )
      }
    }

    "return hotels order by price DESC (GET /hotels?sort=desc)" in {
      val request = HttpRequest(uri = "/hotels/Bangkok?sort=desc")
        .withHeaders(RawHeader("key", RateLimiter.TEST_KEY))
      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`application/json`
        entityAs[Hotels].Values().map(_.Price) shouldEqual List[Double](
          25000, 5300, 2400, 2000, 1000, 900, 60
        )
      }
    }

    "rps should be limited" in {
      val r1 = HttpRequest(uri = "/hotels/Bangkok")
        .withHeaders(RawHeader("key", RateLimiter.TEST_LIMITED_KEY))
      r1 ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
      val r2 = HttpRequest(uri = "/hotels/Bangkok")
        .withHeaders(RawHeader("key", RateLimiter.TEST_LIMITED_KEY))
      r2 ~> routes ~> check {
        rejection shouldEqual RateLimitRejection
      }
      Thread.sleep(1000)
      val r3 = HttpRequest(uri = "/hotels/Bangkok")
        .withHeaders(RawHeader("key", RateLimiter.TEST_LIMITED_KEY))
      r3 ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

  }

}

