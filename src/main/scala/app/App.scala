package app

import app.domain._
import app.gateway.CommandGateway
import zio.{ZIO, ZIOAppArgs, ZIOAppDefault}


object App extends ZIOAppDefault {

  val program = for {
    args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
    commandGateway <- ZIO.service[CommandGateway]
    _ <- commandGateway.execute(args.toList)
  } yield ()

  val run = program.provideSome(CommandGateway.live, CommandService.live)

}
