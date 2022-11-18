package app.infrastructure

import app.domain.Command.{Sum, Divide, Go, Multiply}
import app.domain.{CardinalDirection, Command}
import app.domain.Types.{NonZero, NonZeroInt}
import app.infrastructure.commands.{ArithmeticCommand, MovementCommand}
import eu.timepit.refined._
import eu.timepit.refined.auto._
import monocle.syntax.all._
import scopt.OParser

object CommandParser {

  val builder = OParser.builder[Command]

  val parser = {

    import builder._

    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      ArithmeticCommand.sum,
      ArithmeticCommand.multiplication,
      ArithmeticCommand.divide,
      MovementCommand.go
    )
  }

}
