package app

import scopt.OParser
import zio.{IO, ZIO}

object ZioScopt {

  def parse[T](parser: OParser[_, T], options: List[String], init: T): IO[Option[Nothing], T] =
    ZIO.fromOption(OParser.parse(parser, options, init))

}
