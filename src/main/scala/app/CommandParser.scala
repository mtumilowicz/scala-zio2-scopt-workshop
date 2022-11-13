package app

import app.Command.{Divide, Go, Multiplication, Sum}
import eu.timepit.refined._
import eu.timepit.refined.auto._
import scopt.OParser
import monocle.syntax.all._

object CommandParser {

  val builder = OParser.builder[Command]

  val parser = {

    import builder._

    def xxx(i: Int): NonZeroInt = refineV[NonZero](i)
      .fold(_ => throw new IllegalArgumentException("'" + i + "' cannot be zero."), a => a)

    implicit val nonZeroRead: scopt.Read[NonZeroInt] =
      scopt.Read.intRead.map(xxx)

    implicit val cardinalDirectionRead: scopt.Read[app.CardinalDirection] =
      scopt.Read.reads(CardinalDirection withNameInsensitive  _)

    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      // option -f, --foo
      cmd("sum")
        .action((_, _) => Command.Sum(0, 0))
        .text("sum is an summing property")
        .children(
          opt[Int]("c1")
            .required()
            .action((x, c) => c.asInstanceOf[Sum].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .required()
            .action((x, c) => c.asInstanceOf[Sum].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      cmd("mult")
        .action((_, _) => Command.Multiplication(0, 0))
        .text("mult is an multiplication property")
        .children(
          opt[Int]("c1")
            .required()
            .action((x, c) => c.asInstanceOf[Multiplication].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[Int]("c2")
            .required()
            .action((x, c) => c.asInstanceOf[Multiplication].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      cmd("div")
        .action((_, _) => Command.Divide(0, 1))
        .text("div is an multiplication property")
        .children(
          opt[Int]("c1")
            .required()
            .action((x, c) => c.asInstanceOf[Divide].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[NonZeroInt]("c2")
            .required()
            .action((x, c) => c.asInstanceOf[Divide].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
      cmd("go")
        .action((_, _) => Command.Go(None))
        .text("chose direction to go")
        .children(
          opt[CardinalDirection]("direction")
            .required()
            .action((x, c) => c.asInstanceOf[Go].focus(_.direction).replace(Some(x)))
            .text("direction to go"),
        )
    )
  }

}
