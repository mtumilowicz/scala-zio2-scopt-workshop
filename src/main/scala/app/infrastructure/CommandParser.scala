package app.infrastructure

import app.domain.Command
import app.infrastructure.command.{ArithmeticCommand, MovementCommand}
import scopt.OParser

object CommandParser {

  val builder = OParser.builder[Command]

  val parser = {

    import builder._

    OParser.sequence(
      programName("scopt"),
      head("scala-zio2-scopt-workshop", "1.0.0"),
      help("help"),
      ArithmeticCommand.sum,
      ArithmeticCommand.multiplication,
      ArithmeticCommand.divide,
      ArithmeticCommand.random,
      MovementCommand.go
    )
  }

}
