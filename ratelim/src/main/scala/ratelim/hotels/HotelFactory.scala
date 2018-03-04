package ratelim.hotels

import java.security.InvalidParameterException

object HotelFactory {

  // returns an hotel from a CSV line
  def BuildFromArray(data: Array[String]): Hotel = {
    if (data.length < 4)
      throw new InvalidParameterException("Expected 4 fields to build an hotel.")
    Hotel(data(1).toInt, data(0), data(2), data(3).toDouble)
  }

}
