package race.model

import org.scalatest._
import race.utils.StrictPositive

class RaceSpec extends FlatSpec with Matchers {

  private def getCar(n: Int) = Car(StrictPositive(n))

  private def getRace(length: Int, nbCars: Int): Race = Race(StrictPositive(length), StrictPositive(nbCars))

  "InitialPositions" should "return the initial positions array based on the number of cars" in {
    Race.InitialPositions(StrictPositive(1)) shouldEqual Array(0)
    Race.InitialPositions(StrictPositive(2)) shouldEqual Array(0, -200)
    Race.InitialPositions(StrictPositive(3)) shouldEqual Array(0, -200, -400)
  }

  "InitialCars" should "return the initial cars array based on the number of cars" in {
    Race.InitialCars(StrictPositive(1)) shouldEqual Array(Car(StrictPositive(1)))
    Race.InitialCars(StrictPositive(2)) shouldEqual
      Array(Car(StrictPositive(1)), Car(StrictPositive(2)))
  }

  "NextPositionOf" should "return next position of a car" in {
    Race.NextPositionOf(getCar(1), 0) shouldEqual 0
    val car = getCar(1)
    car.Tick(StrictPositive(2), Array(0))
    Race.NextPositionOf(car, 0) shouldEqual 6.4
    Race.NextPositionOf(car, 100) shouldEqual 106.4
  }

  "Race TestCase #1" should "return expected results" in {
    val race = getRace(1, 1)
    race.Ended shouldEqual false
    race.Tick(StrictPositive(1))
    race.GetTotalDuration shouldEqual 1
    race.GetPositions shouldEqual Array(3.2)
    race.GetRanking shouldEqual Array(1)
    race.Ended shouldEqual true
  }

  "Race TestCase #2" should "return expected results" in {

    val race = getRace(170, 2)

    race.Tick(StrictPositive(10))
    race.GetPositions shouldEqual Array(16, -152.77777777777777)
    race.GetRanking shouldEqual Array(1, 2)
    race.Ended shouldEqual false

    race.Tick(StrictPositive(10))
    race.GetPositions shouldEqual Array(44.8, -82.99999999999999)
    race.GetRanking shouldEqual Array(1, 2)
    race.Ended shouldEqual false

    race.Tick(StrictPositive(60))
    race.GetPositions shouldEqual Array(163.84000000000003, 164.82222222222225) // rank changed
    race.GetRanking shouldEqual Array(2, 1)
    race.Ended shouldEqual false

    race.Tick(StrictPositive(1))
    race.Ended shouldEqual true
  }

}
