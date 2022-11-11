package app

sealed trait Command

object Command {
  case class Sum(c1: Int, c2: Int) extends Command
  case class Multiplication(c1: Int, c2: Int) extends Command
  case object NotProvided extends Command
}
