package app.gateway

import app.domain.{Command, CommandExecutionError, CommandService}
import app.infrastructure.CommandParser
import scopt.{DefaultOEffectSetup, OParser}
import zio.{IO, UIO, ZIO, ZLayer}

case class CommandGateway(service: CommandService) {

  def parse(commands: List[String]): IO[CommandExecutionError, Unit] = {
    if (commands.isEmpty) ZIO.fail(CommandExecutionError.emptyCommand)
    else OParser.runParser(CommandParser.parser, commands, Command.Default) match {
      case (result, effects) =>
        OParser.runEffects(effects, new DefaultOEffectSetup {
          override def terminate(exitState: Either[String, Unit]): Unit = ()
        })
        result match {
          case Some(command) => service.execute(command)
          case _ => ZIO.fail(CommandExecutionError.notSupported(commands.mkString(" ")))
        }
    }
  }
}

object CommandGateway {
  val live = ZLayer.fromZIO {
    for {
      commandService <- ZIO.service[CommandService]
    } yield CommandGateway(commandService)
  }
}
