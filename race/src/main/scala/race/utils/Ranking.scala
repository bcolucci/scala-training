package race.utils

object Ranking {

  def Order(positions: Array[Double]): Array[Option[StrictPositive]] =
    positions.indices.map(_ + 1).map(StrictPositive).map(idx => Of(positions, idx)).toArray

  def Of(positions: Array[Double], teamNum: StrictPositive): Option[StrictPositive] =
    if (teamNum.value <= positions.length) {
      val carPosition = positions(teamNum.value - 1)
      Some(StrictPositive(positions.count(_ > carPosition) + 1))
    }
    else
      None
}
