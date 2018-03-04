package race.utils

case class StrictPositive(value: Int) {

  assume(value > 0)

  override def toString: String = value.toString
}
