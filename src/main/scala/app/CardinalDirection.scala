package app

import enumeratum._

sealed trait CardinalDirection extends EnumEntry

case object CardinalDirection extends Enum[CardinalDirection] with CatsEnum[CardinalDirection] {

  case object North extends CardinalDirection

  case object South extends CardinalDirection

  case object East extends CardinalDirection

  case object West extends CardinalDirection

  val values = findValues

}
