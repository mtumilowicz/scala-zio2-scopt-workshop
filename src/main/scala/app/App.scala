package app

import zio.{ZIO, ZIOAppArgs, ZIOAppDefault}


object App extends ZIOAppDefault {

  def run =
    for {
      args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
      _ <- CommandGateway.execute(args.toList)
    } yield ()


}
