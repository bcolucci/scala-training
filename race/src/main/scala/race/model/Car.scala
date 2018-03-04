package race.model

import race.utils.{Ranking, StrictPositive}

case class Car(teamNum: StrictPositive) {

  // car constants
  val topSpeed: Double = (150 + 10 * teamNum.value) / 3.6
  val acceleration: Int = 2 * teamNum.value

  private var speed: Double = 0
  private var nitroUsed: Boolean = false

  def GetSpeed: Double = speed

  def Tick(duration: StrictPositive, positions: Array[Double]): Unit = {
    accelerate(duration)
    handleSituation(positions)
  }

  private def accelerate(duration: StrictPositive) =
    Car UpdateSpeedAfterAcceleration(this, duration)

  private def handleSituation(positions: Array[Double]): Unit = {
    Car UpdateSpeedAfterHandling(this, Car.HANDLING_FACTOR)
    Car CarsInProximity(this, positions, Car.CARS_WITHIN_INTERVAL)
    if (!nitroUsed && isTheLast(positions))
      useNitro()
  }

  private def isTheLast(positions: Array[Double]) = {
    val ranking = Ranking Of(positions, teamNum)
    ranking.get.value == positions.length
  }

  private def useNitro(): Unit = {
    println(s"#$teamNum uses nitro")
    Car UpdateSpeedAfterNitro this
    nitroUsed = true
  }

  override def toString: String = "" +
    s"#$teamNum topSpeed:$topSpeed m/s" +
    s", acceleration:$acceleration m/s^2" +
    s", speed:$speed m/s"
}

object Car {

  // constants
  val HANDLING_FACTOR: Double = 0.8
  val CARS_WITHIN_INTERVAL: Int = 10

  // Updates and returns the car updated speed after an acceleration
  def UpdateSpeedAfterAcceleration(car: Car, duration: StrictPositive): Double = {
    car.speed += car.acceleration * duration.value
    car.speed
  }

  // Updates and returns the car updated speed after the driver handle the situation
  def UpdateSpeedAfterHandling(car: Car, handleInterval: Double): Double = {
    car.speed *= handleInterval
    car.speed
  }

  // Updates and returns the car updated speed after using the nitro
  def UpdateSpeedAfterNitro(car: Car): Double = {
    car.speed = math min(car.topSpeed, car.speed * 2)
    car.speed
  }

  // Returns the array of nearest cars teamId
  def CarsInProximity(car: Car, positions: Array[Double], interval: Int): Array[StrictPositive] = {
    val carPosition = positions(car.teamNum.value - 1)
    val minPosition = carPosition - interval
    val maxPosition = carPosition + interval
    positions.indices
      .filter(_ + 1 != car.teamNum.value)
      .filter(idx => {
        val position = positions(idx)
        position >= minPosition && position <= maxPosition
      })
      .map(_ + 1)
      .map(StrictPositive)
      .toArray
  }

}