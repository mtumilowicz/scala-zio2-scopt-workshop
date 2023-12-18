package app.domain

import cats.implicits.showInterpolator
import zio.{Console, IO, ZIO, ZLayer}

import java.io.IOException

class CommandService {

  val random = new scala.util.Random

  def execute(command: Command): IO[CommandExecutionError, Unit] = (command match {
    case Command.Sum(c1, c2) => Console.printLine(c1 + c2)
    case Command.Multiply(c1, c2) => Console.printLine(c1 * c2)
    case Command.Divide(c1, c2) => Console.printLine(c1 / c2.value)
    case Command.Random(negative) => Console.printLine(rand(negative))
    case Command.Go(direction) => direction match {
      case Some(dir) => Console.printLine(show"going into $dir")
      case None => Console.printLine(show"no direction home")
    }
    case Command.Default => ZIO.unit
  }).mapError(CommandExecutionError.fromError)

  private def rand(negative: Boolean): Int =
    if (negative) {
      random.between(Int.MinValue, 0)
    } else {
      random.between(0, Int.MaxValue)
    }

}

object CommandService {
  val live = ZLayer.succeed(new CommandService())
}
