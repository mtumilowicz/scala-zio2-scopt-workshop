package app.domain

import app.domain.Types.NonZeroInt

sealed trait Command

object Command {
  case class Sum(component1: Int, component2: Int) extends Command
  case class Multiply(factor1: Int, factor2: Int) extends Command
  case class Divide(dividend: Int, divisor: NonZeroInt) extends Command
  case class Random(negative: Boolean) extends Command
  case class Go(direction: Option[CardinalDirection]) extends Command
  case object Default extends Command
  case object Help extends Command
}
