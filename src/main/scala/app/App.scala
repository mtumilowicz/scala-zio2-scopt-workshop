package app

import zio.{ZIO, ZIOAppArgs, ZIOAppDefault}


object App extends ZIOAppDefault {

  val program = for {
    args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
    commandGateway <- ZIO.service[CommandGateway]
    _ <- commandGateway.execute(args.toList)
  } yield ()

  def run = program.provideSome(CommandGateway.live, CommandService.live)

}
