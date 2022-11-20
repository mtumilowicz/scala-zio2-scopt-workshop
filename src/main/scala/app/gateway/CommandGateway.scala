package app.gateway

import app.domain.{Command, CommandExecutionError, CommandService}
import app.infrastructure.CommandParser
import scopt.OParser
import zio.{IO, UIO, ZIO, ZLayer}

case class CommandGateway(service: CommandService) {

  def parse(commands: List[String]): IO[CommandExecutionError, Unit] =
    OParser.parse(CommandParser.parser, commands, Command.Default) match {
      case Some(command) => service.execute(command)
      case None => ZIO.fail(CommandExecutionError.notSupported(commands.mkString(" ")))
    }
}

object CommandGateway {
  val live = ZLayer.fromZIO {
    for {
      commandService <- ZIO.service[CommandService]
    } yield CommandGateway(commandService)
  }
}
