package app

import app.Command.{Divide, Multiplication, Sum}
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import eu.timepit.refined.predicates.all.Or
import scopt.OParser

object CommandParser {

  val builder = OParser.builder[Command]

  val parser = {

    import builder._

    def xxx(i: Int): NonZeroInt = refineV[NonZero](i)
      .fold(_ => throw new IllegalArgumentException("'" + i + "' cannot be zero."), a => a)

    implicit val nonZeroRead: scopt.Read[NonZeroInt] =
      scopt.Read.intRead.map(xxx)

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
        .action((_, _) => Command.Multiplication(0, 0))
        .text("mult is an multiplication property")
        .children(
          opt[Int]("c1")
            .required()
            .action((x, c) => c.asInstanceOf[Multiplication].copy(c1 = x))
            .text("c1 is a Int property"),
          opt[NonZeroInt]("c2")
            .required()
            .action((x, c) => c.asInstanceOf[Divide].copy(c2 = x))
            .text("c2 is a Int property"),
        ),
    )
  }

}