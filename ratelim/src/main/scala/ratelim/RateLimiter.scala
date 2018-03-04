package ratelim

import akka.http.scaladsl.server.Rejection

import scala.concurrent.duration._

case object RateLimitRejection extends Rejection

final case class RateLimiter(
    maxRPS: Double = RateLimiter.DEFAULT_MAX_RPS,
    suspensionTime: Long = RateLimiter.DEFAULT_SUSPENSION_TIME
) {

  private var entries = Map.empty[String, RateLimitEntry]

  def Keys(): Set[String] = entries.keySet

  def Add(key: String, maxRPS: Double = this.maxRPS): RateLimitEntry = {
    val entry = RateLimitEntry(maxRPS, suspensionTime)
    entries += (key -> entry)
    entry
  }

  def IsAuthorized(key: String): Boolean = Get(key) match {
    case None => false
    case option =>
      val entry = option.get
      entries += (key -> entry.Update())
      !entry.Exceed
  }

  def Get(key: String): Option[RateLimitEntry] = entries get key

}

object RateLimiter {

  val DEFAULT_MAX_RPS: Double = 10
  val DEFAULT_SUSPENSION_TIME: Long = (5 minutes).length

  val TEST_KEY = "cZhAYZ1OFyaIQRKxuIVRS1Fb8wz81Fpf"
  val TEST_LIMITED_KEY = "IgU6hjsYscnFST3ZeZ9WvQBqceFQae6E"

  private val instance = RateLimiter()

  // hardcoded api keys
  instance.Add(TEST_KEY, 100)
  instance.Add(TEST_LIMITED_KEY, 0.5) // 1 req/2s

  def Add(key: String, maxRPS: Double = instance.maxRPS): RateLimitEntry = instance.Add(key, maxRPS)

  def IsAuthorized(key: String): Boolean = instance.IsAuthorized(key)

}