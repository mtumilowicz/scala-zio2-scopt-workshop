package app.domain

import cats.implicits.showInterpolator
import zio.{Console, IO, ZIO, ZLayer}

import java.io.IOException

class CommandService {

  val random = new scala.util.Random

  def execute(command: Command) = command match {
    case Command.Sum(c1, c2) => wrap(Console.printLine(c1 + c2))
    case Command.Multiply(c1, c2) => wrap(Console.printLine(c1 * c2))
    case Command.Divide(c1, c2) => wrap(Console.printLine(c1 / c2.value))
    case Command.Random(negative) => wrap(Console.printLine(rand(negative)))
    case Command.Go(direction) => direction match {
      case Some(dir) => wrap(Console.printLine(show"going into $dir"))
      case None => wrap(Console.printLine(show"no direction home"))
    }
    case Command.Help => ZIO.unit
    case Command.Default => CommandExecutionError.zio(CommandExecutionError.emptyCommand.message)
  }

  private def rand(negative: Boolean): Int =
    if (negative) {
      random.between(Int.MinValue, 0)
    } else {
      random.between(0, Int.MaxValue)
    }

  private def wrap(execution: IO[IOException, Unit]): IO[CommandExecutionError, Unit] =
    execution.mapError(CommandExecutionError.fromError)

}

object CommandService {
  val live = ZLayer.succeed(new CommandService())
}
