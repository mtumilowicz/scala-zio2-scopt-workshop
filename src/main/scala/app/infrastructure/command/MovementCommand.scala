package app.infrastructure.command

import app.domain.Command.Go
import app.domain.{CardinalDirection, Command}
import app.infrastructure.scoptreaders.CardinalDirectionReader._
import scopt.OParser

object MovementCommand {

  val builder = OParser.builder[Command]
  import builder._

  val go = cmd("go")
    .action((_, _) => Command.Go(None))
    .text("go - go into chosen direction")
    .children(
      opt[CardinalDirection]("direction")
        .required()
        .action((value, command) => command.asInstanceOf[Go].copy(direction = Some(value)))
        .text("direction to go: south, east, west, north"),
    )

}
