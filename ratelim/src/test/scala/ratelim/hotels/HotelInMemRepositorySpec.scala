package ratelim.hotels

import org.scalatest.{ FlatSpec, Matchers }
import ratelim.utils.SortOrder

import scala.collection.SortedSet

class HotelInMemRepositorySpec extends FlatSpec with Matchers {

  private val repository = "The inMemory repository"

  repository should "throw an error if the CSV file does not exist" in {
    intercept[NullPointerException] {
      HotelInMemRepository("fileDoesNotExist.csv")
    }
  }

  repository should "return an empty set for a city that does not exist" in {
    val hotels = HotelInMemRepository("hoteldb.csv")
    hotels.Search("cityDoesNotExist") shouldEqual Set[Hotel]()
    (hotels.Search("Bangkok").map(_.Id) --
      Set[Int](8, 15, 11, 6, 1, 14, 18)) shouldBe empty
  }

  repository should "search for city hotels" in {
    val hotels = HotelInMemRepository("hoteldb.csv")
    hotels.Search("Bangkok").map(_.Price) shouldBe SortedSet[Double](
      5300, 60, 900, 25000, 2000, 1000, 2400
    )
  }

  repository should "search for city hotels order by price (asc)" in {
    val hotels = HotelInMemRepository("hoteldb.csv")
    hotels.Search("Bangkok", SortOrder.ASC).map(_.Price) shouldBe SortedSet[Double](
      60, 900, 1000, 2000, 2400, 5300, 25000
    )
  }

  repository should "search for city hotels order by price (desc)" in {
    val hotels = HotelInMemRepository("hoteldb.csv")
    hotels.Search("Bangkok", SortOrder.DESC).map(_.Price) shouldBe SortedSet[Double](
      25000, 5300, 2400, 2000, 1000, 900, 60
    )
  }

}
