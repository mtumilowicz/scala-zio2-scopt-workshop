package app.gateway

import app.domain.{Command, CommandExecutionError, CommandService}
import app.infrastructure.{CommandParser, ScoptZio}
import zio.{IO, ZIO, ZLayer}

case class CommandGateway(service: CommandService) {

  def execute(commands: List[String]): IO[CommandExecutionError, Unit] = {
    if (commands.isEmpty) ZIO.fail(CommandExecutionError.emptyCommand)
    else ScoptZio.runParser(CommandParser.parser, commands, Command.Default)
      .some.flatMap(service.execute)
      .orElseFail(CommandExecutionError.notSupported(commands.mkString(" ")))
  }

  // only for demonstration: using that method hangs out app when command is --help due to scopt internal behaviour
  def parseHangsOut(commands: List[String]): IO[CommandExecutionError, Unit] = {
    if (commands.isEmpty) ZIO.fail(CommandExecutionError.emptyCommand)
    else ScoptZio.runParserHangsOut(CommandParser.parser, commands, Command.Default)
      .flatMap(service.execute)
      .orElseFail(CommandExecutionError.notSupported(commands.mkString(" ")))
  }
}

object CommandGateway {
  val live = ZLayer.fromZIO {
    for {
      commandService <- ZIO.service[CommandService]
    } yield CommandGateway(commandService)
  }
}
