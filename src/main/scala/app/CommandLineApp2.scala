package app

import app.Command.{MultiplicationCommand, SumCommand}
import scopt.OParser
import zio.{ZIO, ZIOAppArgs, ZIOAppDefault, Console}


object CommandLineApp2 extends ZIOAppDefault {

  val builder = OParser.builder[Command]

  val parser = {
    import builder._

    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      // option -f, --foo
      cmd("sum")
        .action((_, _) => Command.SumCommand(0, 0))
        .text("sum is an summing property")
        .children(
          opt[Int]("c1")
            .action((x, c) => c.asInstanceOf[SumCommand].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .action((x, c) => c.asInstanceOf[SumCommand].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      cmd("mult")
        .action((_, _) => Command.MultiplicationCommand(0, 0))
        .text("mult is an multiplication property")
        .children(
          opt[Int]("c1")
            .action((x, c) => c.asInstanceOf[MultiplicationCommand].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .action((x, c) => c.asInstanceOf[MultiplicationCommand].copy(c2 = x))
            .text("c2 is a Int property"),
        )
      // more options here...
    )
  }

  def run =
    for {
      args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
      _ <- OParser.parse(parser, args, Command.Noop) match {
        case Some(cmd) => cmd match {
          case SumCommand(c1, c2) => Console.printLine(c1 + c2)
          case MultiplicationCommand(c1, c2) => Console.printLine(c1 * c2)
          case Command.Noop => Console.printLine("noop")
        }
        case None => Console.printLine("noop")
      }
    } yield ()


}
