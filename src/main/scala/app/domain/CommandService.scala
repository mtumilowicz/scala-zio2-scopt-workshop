package app.domain

import cats.implicits.showInterpolator
import zio.{Console, IO, ZLayer}

import java.io.IOException

class CommandService {

  def execute(command: Command) = command match {
    case Command.Sum(c1, c2) => wrap(Console.printLine(c1 + c2))
    case Command.Multiply(c1, c2) => wrap(Console.printLine(c1 * c2))
    case Command.Divide(c1, c2) => wrap(Console.printLine(c1 / c2.value))
    case Command.Go(direction) => direction match {
      case Some(dir) => Console.printLine(show"going into $dir")
      case None => Console.printLine(show"no direction home")
    }
    case Command.Default => CommandExecutionError.zio(CommandExecutionError.emptyCommand.message)
  }

  private def wrap(execution: IO[IOException, Unit]): IO[CommandExecutionError, Unit] =
    execution.mapError(CommandExecutionError.fromError)

}

object CommandService {
  val live = ZLayer.succeed(new CommandService())
}
