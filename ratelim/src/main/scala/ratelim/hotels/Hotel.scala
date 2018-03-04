package ratelim.hotels

final case class Hotel(Id: Int, CityId: String, Room: String, Price: Double) {
  override def toString: String = s"Hotel #$Id, city:$CityId, room:$Room, price:$Price"
}

final case class Hotels(hotels: Seq[Hotel]) {
  def Values(): Seq[Hotel] = hotels
}

