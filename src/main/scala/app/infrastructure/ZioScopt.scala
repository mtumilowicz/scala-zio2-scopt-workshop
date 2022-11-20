package app.infrastructure

import scopt.OParser
import zio.{IO, ZIO}

object ZioScopt {

  def parse[T](parser: OParser[_, T], options: Seq[String], init: T): IO[Option[Nothing], T] = {
    val result = OParser.parse(parser, options, init)
    ZIO.fromOption(result)
  }

}
