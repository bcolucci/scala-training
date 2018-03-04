package race

import race.model.Race
import race.utils.StrictPositive

object boot extends App {

  val raceLength = StrictPositive(400)
  val nbCars = StrictPositive(6)
  val race = Race(raceLength, nbCars)

  println(s"positions before: ${race.GetPositions.mkString(",")}")

  while (!race.Ended)
    race Tick Race.TICK_INTERVAL

  0 to 100 foreach (_ => print("-"))
  println()

  race.GetCars.foreach(println)

  println(s"race duration: ${race.GetTotalDuration}s")
  println(s"positions after: ${race.GetPositions.mkString(",")}")
  race.GetRanking.zipWithIndex.foreach {
    case (r, n) => println(s"\t#${n + 1} rank: ${r}")
  }

}

