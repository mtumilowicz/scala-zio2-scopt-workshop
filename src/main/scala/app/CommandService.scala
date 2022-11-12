package app

import app.Command.{Divide, Multiplication, Sum}
import zio.{Console, ExitCode, IO, UIO, ZIO, ZLayer}

import java.io.IOException

class CommandService {

  def execute(command: Command) = command match {
    case Sum(c1, c2) => wrap(Console.print(c1 + c2))
    case Multiplication(c1, c2) => wrap(Console.print(c1 * c2))
    case Divide(c1, c2) => wrap(Console.print(c1 / c2.value))
    case Command.Default => CommandExecutionError.zio(CommandExecutionError.emptyCommand.message)
  }

  private def wrap(execution: IO[IOException, Unit]): IO[CommandExecutionError, Unit] =
    execution.mapError(CommandExecutionError.fromError)

}

object CommandService {
  val live = ZLayer.succeed(new CommandService())
}
