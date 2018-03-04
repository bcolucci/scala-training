package race.model

import org.scalatest._
import race.utils.StrictPositive

class CarSpec extends FlatSpec with Matchers {

  private def getCar(n: Int) = Car(StrictPositive(n))

  "Car initial top speed" should "respect the problem constraint" in {
    getCar(1).topSpeed shouldEqual 44.44444444444444
    getCar(2).topSpeed shouldEqual 47.22222222222222
  }

  "Car initial acceleration" should "respect the problem constraint" in {
    getCar(1).acceleration shouldEqual 2
    getCar(2).acceleration shouldEqual 4
  }

  "Car initial speed" should "be 0" in {
    getCar(1).GetSpeed shouldEqual 0
  }

  "UpdateSpeedAfterAcceleration" should "increase the speed" in {
    // start with speed 0 each time
    Car.UpdateSpeedAfterAcceleration(getCar(1), StrictPositive(1)) shouldEqual 2
    Car.UpdateSpeedAfterAcceleration(getCar(1), StrictPositive(2)) shouldEqual 4
    // continuous acceleration
    Car.UpdateSpeedAfterAcceleration(getCar(1), StrictPositive(5)) shouldEqual 10
    // same car, multiple accelerations
    val car = getCar(1)
    Car.UpdateSpeedAfterAcceleration(car, StrictPositive(1)) shouldEqual 2
    Car.UpdateSpeedAfterAcceleration(car, StrictPositive(1)) shouldEqual 4
    Car.UpdateSpeedAfterAcceleration(car, StrictPositive(1)) shouldEqual 6
    Car.UpdateSpeedAfterAcceleration(car, StrictPositive(2)) shouldEqual 10
  }

  "When car handle, it" should "reduce the speed" in {
    val car = getCar(1)
    Car.UpdateSpeedAfterAcceleration(car, StrictPositive(10))
    Car.UpdateSpeedAfterHandling(car, 0.5) shouldEqual 10
    Car.UpdateSpeedAfterHandling(car, 0.5) shouldEqual 5
  }

  "UpdateSpeedAfterNitro" should "increase speed" in {
    // x2 case
    val car1 = getCar(1)
    Car.UpdateSpeedAfterAcceleration(car1, StrictPositive(10))
    Car.UpdateSpeedAfterNitro(car1)
    car1.GetSpeed shouldEqual 40
    // top speed case
    val car2 = getCar(1)
    Car.UpdateSpeedAfterAcceleration(car2, StrictPositive(15))
    Car.UpdateSpeedAfterNitro(car2)
    car2.GetSpeed shouldEqual car2.topSpeed
  }

  "CarsInProximity" should "should returns cars in proximity of another" in {
    Car.CarsInProximity(getCar(1), Array(0), 10) shouldEqual Array()
    Car.CarsInProximity(getCar(1), Array(0, 15), 10) shouldEqual Array()
    Car.CarsInProximity(getCar(1), Array(0, 10), 10).map(_.value) shouldEqual Array(2)
    Car.CarsInProximity(getCar(1), Array(-100, 50, -95, -105, 250), 5).map(_.value) shouldEqual Array(3, 4)
  }

}
