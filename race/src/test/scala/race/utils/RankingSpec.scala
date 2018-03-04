package race.utils

import org.scalatest._

class RankingSpec extends FlatSpec with Matchers {

  private val positions = Array[Double](6, 3, 8, 1)

  "Of" should "return None if teamId is invalid or does not exist" in {
    Ranking.Of(Array(), StrictPositive(1)) shouldEqual None
    Ranking.Of(Array(1, 2, 3), StrictPositive(4)) shouldEqual None
  }

  "Of" should "return the rank of a team based on car positions" in {
    Ranking.Of(positions, StrictPositive(1)).get.value shouldEqual 2
    Ranking.Of(positions, StrictPositive(2)).get.value shouldEqual 3
    Ranking.Of(positions, StrictPositive(3)).get.value shouldEqual 1
    Ranking.Of(positions, StrictPositive(4)).get.value shouldEqual 4
  }

  "Order" should "return the ordered array of ranking based on car positions" in {
    Ranking.Order(positions).flatten.map(_.value) shouldEqual Array(2, 3, 1, 4)
  }

}
