package app.infrastructure

import scopt.{DefaultOEffectSetup, OEffect, OParser}
import zio.{IO, Task, ZIO}

object ScoptZio {

  private val defaultEffectSetup = new DefaultOEffectSetup {
    // we have to override terminate
    // by default, when the --help or --version are invoked, they call sys.exit(0) after printing
    // it causes ZIO to hang out indefinitely
    override def terminate(exitState: Either[String, Unit]): Unit = ()
  }

  def runParser[C](parser: OParser[_, C], commands: Seq[String], init: C): Task[Option[C]] = for {
    (maybeCommand, effects) <- ZIO.attempt(OParser.runParser(parser, commands, init))
    _ <- runEffects(effects)
  } yield maybeCommand

  // only for demonstration: using that method hangs out app when command is --help due to scopt internal behaviour
  def runParserHangsOut[C](parser: OParser[_, C], commands: Seq[String], init: C): IO[Option[Nothing], C] =
    ZIO.fromOption(OParser.parse(parser, commands, init))

  private def runEffects(es: List[OEffect]): Task[Unit] =
    ZIO.attempt(OParser.runEffects(es, defaultEffectSetup))

}
