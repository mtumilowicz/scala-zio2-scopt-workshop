package app

import app.Command.{Multiplication, Sum}
import scopt.OParser
import zio.{Console, ExitCode, ZIO, ZIOAppArgs, ZIOAppDefault}


object CommandLineApp2 extends ZIOAppDefault {

  val builder = OParser.builder[Command]

  val parser = {
    import builder._

    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      // option -f, --foo
      cmd("sum")
        .action((_, _) => Command.Sum(0, 0))
        .text("sum is an summing property")
        .children(
          opt[Int]("c1")
            .action((x, c) => c.asInstanceOf[Sum].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .action((x, c) => c.asInstanceOf[Sum].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      cmd("mult")
        .action((_, _) => Command.Multiplication(0, 0))
        .text("mult is an multiplication property")
        .children(
          opt[Int]("c1")
            .action((x, c) => c.asInstanceOf[Multiplication].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .action((x, c) => c.asInstanceOf[Multiplication].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      checkConfig {
        case Command.NotProvided => failure("no command provided")
        case _ => success
      }
      // more options here...
    )
  }

  def run =
    for {
      args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
      command <- ZioScopt.parse(parser, args.toList, Command.NotProvided)
      _ <- CommandService.execute(command)
    } yield ()


}
