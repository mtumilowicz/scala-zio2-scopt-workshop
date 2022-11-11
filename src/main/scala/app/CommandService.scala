package app

import app.Command.{Multiplication, Sum}
import zio.{Console, ExitCode, IO, UIO, ZIO}

import java.io.IOException

object CommandService {

  def execute(command: Command) = command match {
    case Sum(c1, c2) => wrap(Console.printLine(c1 + c2))
    case Multiplication(c1, c2) => wrap(Console.printLine(c1 * c2))
    case Command.Default => CommandExecutionError.zio(CommandExecutionError.emptyCommand.message)
  }

  private def wrap(execution: IO[IOException, Unit]): IO[CommandExecutionError, Unit] =
    execution.mapError(CommandExecutionError.fromError)

}
