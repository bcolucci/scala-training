package ratelim

import org.scalatest.{ FlatSpec, Matchers }

class RateLimitEntrySpec extends FlatSpec with Matchers {

  "RateLimitEntry" should "should limite req calls" in {
    val entry = RateLimitEntry(2, 1000) // 2 req/s, 1 sec of suspension
    entry.Update()
    entry.Exceed() shouldEqual false
    Thread.sleep(500)
    entry.Update()
    entry.Exceed() shouldEqual false
    Thread.sleep(600)
    entry.Update()
    entry.Exceed() shouldEqual true
    Thread.sleep(500)
    entry.Update()
    entry.Exceed() shouldEqual true
    Thread.sleep(600) // suspension is over
    entry.Update()
    entry.Exceed() shouldEqual false
  }

}