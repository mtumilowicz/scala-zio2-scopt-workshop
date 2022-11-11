package app

sealed trait Command

object Command {
  case class SumCommand(c1: Int, c2: Int) extends Command
  case class MultiplicationCommand(c1: Int, c2: Int) extends Command
  case object Noop extends Command
}
