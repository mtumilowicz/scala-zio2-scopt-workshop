package app

object CommandGateway {

  def execute(commands: List[String]) =
    parse(commands).flatMap(CommandService.execute)

  private def parse(commands: List[String]) =
    ZioScopt.parse(CommandParser.parser, commands, Command.Default)
      .orElseFail(CommandExecutionError.notSupported)

}
