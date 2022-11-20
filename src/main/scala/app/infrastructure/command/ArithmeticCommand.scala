package app.infrastructure.command

import app.domain.Command
import app.domain.Command.{Divide, Multiply, Random, Sum}
import app.domain.Types.NonZeroInt
import app.infrastructure.scoptreaders.RefinedReader.nonZeroRead
import eu.timepit.refined.auto._
import scopt.OParser

object ArithmeticCommand {

  val builder = OParser.builder[Command]
  import builder._

  val sum = cmd("sum")
    .action((_, _) => Command.Sum(0, 0))
    .text("sum(a, b) = a + b")
    .children(
      opt[Int]("c1")
        .required()
        .action((value, command) => command.asInstanceOf[Sum].copy(component1 = value))
        .text("c1 is an Int property"),
      opt[Int]("c2")
        .required()
        .action((value, command) => command.asInstanceOf[Sum].copy(component2 = value))
        .text("c2 is an Int property"),
    )

  val multiplication = cmd("mult")
    .action((_, _) => Command.Multiply(0, 0))
    .text("mult(a,b) = a + b")
    .children(
      opt[Int]("c1")
        .required()
        .action((value, command) => command.asInstanceOf[Multiply].copy(factor1 = value))
        .text("c1 is an Int property"),
      opt[Int]("c2")
        .required()
        .action((value, command) => command.asInstanceOf[Multiply].copy(factor2 = value))
        .text("c2 is an Int property"),
    )

  val divide = cmd("div")
    .action((_, _) => Command.Divide(0, 1))
    .text("div(a, b) = a / b")
    .children(
      opt[Int]("d")
        .required()
        .action((value, command) => command.asInstanceOf[Divide].copy(dividend = value))
        .text("d = dividend, is an Int property"),
      opt[NonZeroInt]("dd")
        .required()
        .action((value, command) => command.asInstanceOf[Divide].copy(divisor = value))
        .text("dd = divisor, is an Int property"),
    )

  val random = cmd("random")
      .abbr("rand")
      .action((_, _) => Command.Random(negative = false))
      .text("generates positive/negative random number")
      .children(
        opt[Unit]("positive")
          .abbr("p")
          .action((_, c) => c.asInstanceOf[Random].copy(negative = false))
          .text("random positive number"),
        opt[Unit]("negative")
          .abbr("n")
          .action((_, c) => c.asInstanceOf[Random].copy(negative = true))
          .text("random negative number"),
      )

}
