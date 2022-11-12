package app

import org.slf4j.LoggerFactory
import scopt.{DefaultOEffectSetup, OParser}
import zio.{IO, ZIO}

object ZioScopt {

  private val logger = LoggerFactory.getLogger(classOf[Nothing])
  private val effectSetup = new DefaultOEffectSetup {
    override def displayToOut(msg: String): Unit = logger.info(msg)

    override def displayToErr(msg: String): Unit = logger.error(msg)
    // override def reportError(msg: String): Unit = displayToErr("Error: " + msg)
    // override def reportWarning(msg: String): Unit = displayToErr("Warning: " + msg)

    // ignore terminate
    override def terminate(exitState: Either[String, Unit]): Unit = ()
  }

  def parse[T](parser: OParser[_, T], options: List[String], init: T): IO[Option[Nothing], T] =
    ZIO.fromOption(OParser.runParser(parser, options, init) match {
      case (result, effects) =>
        OParser.runEffects(effects, effectSetup)
        result
    })

}
