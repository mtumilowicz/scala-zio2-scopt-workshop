package app

import scopt.OParser
import zio.{IO, UIO, URIO, ZIO}

object ZioScopt {

  def parse[T](parser: OParser[_, T], options: List[String], noCommand: T) =
    ZIO.fromOption(OParser.parse(parser, options, noCommand))
      .orElseSucceed(noCommand)

}
