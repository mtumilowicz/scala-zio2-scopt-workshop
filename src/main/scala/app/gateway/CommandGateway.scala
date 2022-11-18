package app.gateway

import app.domain.{Command, CommandExecutionError, CommandService}
import app.infrastructure.{CommandParser, ZioScopt}
import zio.{ZIO, ZLayer}

case class CommandGateway(service: CommandService) {

  def execute(commands: List[String]): ZIO[Any, Object, Unit] =
    parse(commands).flatMap(service.execute)

  private def parse(commands: List[String]) =
    ZioScopt.parse(CommandParser.parser, commands, Command.Default)
      .orElseFail(CommandExecutionError.notSupported)
}

object CommandGateway {
  val live = ZLayer.fromZIO {
    for {
      commandService <- ZIO.service[CommandService]
    } yield CommandGateway(commandService)
  }
}
