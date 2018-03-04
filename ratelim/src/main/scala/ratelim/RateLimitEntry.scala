package ratelim

import java.time.Instant

final case class RateLimitEntry(maxRPS: Double, suspensionTime: Long) {

  private var counter: Int = 0
  private var exceed: Long = 0
  private var updatedAt: Long = Instant.now.getEpochSecond

  def Update(): RateLimitEntry = {
    updatedAt = Instant.now.toEpochMilli
    if (Exceed()) checkForReset() else checkIfExceed()
    if (!Exceed()) counter += 1
    this
  }

  def Exceed(): Boolean = exceed > 0

  private def checkForReset(): Unit = {
    val exceedSince = Instant.now.toEpochMilli - exceed
    if (exceedSince >= suspensionTime) {
      counter = 0
      exceed = 0
    }
  }

  private def checkIfExceed(): Unit = if (rps >= maxRPS) {
    exceed = Instant.now.toEpochMilli
  }

  private def rps(): Double = counter match {
    case 0 => 0
    case c => c / Math.max(1, ellapsed())
  }

  def ellapsed(): Long = Instant.now.toEpochMilli - updatedAt
}

