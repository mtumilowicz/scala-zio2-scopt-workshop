package app.infrastructure.commands

import app.domain.Command.Go
import app.domain.{CardinalDirection, Command}
import app.infrastructure.scoptreaders.CardinalDirectionReader._
import monocle.syntax.all._
import scopt.OParser

object MovementCommand {

  val builder = OParser.builder[Command]
  import builder._

  val go = cmd("go")
    .action((_, _) => Command.Go(None))
    .text("go - go into chosen direction")
    .children(
      opt[CardinalDirection]("dir")
        .required()
        .action((value, command) => command.asInstanceOf[Go].focus(_.direction).replace(Some(value)))
        .text("dir is a direction to go"),
    )

}
