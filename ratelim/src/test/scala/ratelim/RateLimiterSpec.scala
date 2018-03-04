package ratelim

import org.scalatest.{ FlatSpec, Matchers }

class RateLimiterSpec extends FlatSpec with Matchers {

  "RateLimiter" should "should limit requests (standalone)" in {

    val limiter = RateLimiter(2, 1000) // 2 req/s and 1 sec of suspension by default

    limiter.Add("first")
    limiter.Add("second", 3)

    limiter.IsAuthorized("first") shouldEqual true
    limiter.IsAuthorized("second") shouldEqual true

    Thread.sleep(900)
    limiter.IsAuthorized("first") shouldEqual true
    limiter.IsAuthorized("second") shouldEqual true

    Thread.sleep(100)
    limiter.IsAuthorized("first") shouldEqual false
    limiter.IsAuthorized("second") shouldEqual true

    Thread.sleep(100)
    limiter.IsAuthorized("first") shouldEqual false
    limiter.IsAuthorized("second") shouldEqual false

    Thread.sleep(1000) // suspension is over

    limiter.IsAuthorized("first") shouldEqual true
    limiter.IsAuthorized("second") shouldEqual true
  }

}