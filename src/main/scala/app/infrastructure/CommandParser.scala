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
      head("scopt", "4.x"),
      help("help").action((_, _) => Command.Help),
      ArithmeticCommand.sum,
      ArithmeticCommand.multiplication,
      ArithmeticCommand.divide,
      ArithmeticCommand.random,
      MovementCommand.go
    )
  }

}
