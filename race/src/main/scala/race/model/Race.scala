package race.model

import race.utils._

case class Race(length: StrictPositive, nbCars: StrictPositive) {

  private var ranking = Race InitialRanking nbCars
  private var positions = Race InitialPositions nbCars
  private var cars = Race InitialCars nbCars
  private var totalDuration: Int = 0

  def GetRanking: Array[Int] = ranking

  def GetTotalDuration: Int = totalDuration

  def GetCars: Array[Car] = cars

  def Tick(duration: StrictPositive): Unit = {

    println("\t_-= Tick =-_")

    totalDuration += duration.value

    // tick cars
    cars foreach (_ Tick(duration, GetPositions))
    //cars foreach println

    // updating positions
    positions.zipWithIndex foreach {
      case (p, idx) => positions update(idx, Race NextPositionOf(cars(idx), p))
    }
    println(s"positions: ${positions.mkString(",")}")

    // updating ranking
    ranking = Ranking.Order(positions).flatten map (_.value)
    println(s"ranking: ${ranking.mkString(",")}")

  }

  def GetPositions: Array[Double] = positions

  def Ended: Boolean = positions.count(_ >= length.value) > 0
}

object Race {

  // constants
  val POSITION_SPACE: Double = 200
  val TICK_INTERVAL = StrictPositive(2)

  // Returns the initial ranking
  def InitialRanking(nbCars: StrictPositive): Array[Int] = (0 until nbCars.value).toArray

  // Return the initial cars positions array
  def InitialPositions(nbCars: StrictPositive): Array[Double] = (
    for (n <- 0 until nbCars.value) yield -n * POSITION_SPACE
    ).toArray

  // Returns the initial cars array
  def InitialCars(nbCars: StrictPositive): Array[Car] = (
    for (n <- 1 to nbCars.value) yield Car(StrictPositive(n))
    ).toArray

  // Returns car next position based on it's current position & speed
  def NextPositionOf(car: Car, position: Double): Double = position + car.GetSpeed
}