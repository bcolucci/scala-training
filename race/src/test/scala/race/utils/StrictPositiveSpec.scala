package race.utils

import org.scalatest._

class StrictPositiveSpec extends FlatSpec with Matchers {

  "StrictPositive number" should "be > 0" in {
    intercept[AssertionError] {
      StrictPositive(-1)
    }
    intercept[AssertionError] {
      StrictPositive(0)
    }
    StrictPositive(1).value shouldEqual 1
  }

}
