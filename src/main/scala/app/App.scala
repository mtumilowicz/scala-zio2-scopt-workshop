package app

import app.domain._
import app.gateway.CommandGateway
import app.infrastructure._
import zio.{ZIO, ZIOAppArgs, ZIOAppDefault}


object App extends ZIOAppDefault {

  // bug in ZIO: hangs out indefinitely (after execution) when you use getArgs
//  val hangOuts = for {
//    args <- getArgs
//    _ <- ZioScopt.parse(CommandParser.parser, Seq("--help"), Command.Default)
//  } yield ()
//
//  val notHangOuts = for {
//    _ <- ZioScopt.parse(CommandParser.parser, Seq("--help"), Command.Default)
//  } yield ()

  val program = for {
    args <- ZIO.serviceWith[ZIOAppArgs](_.getArgs)
    commandGateway <- ZIO.service[CommandGateway]
    _ <- commandGateway.parse(args.toList)
  } yield ()

  val run = program.provideSome(CommandGateway.live, CommandService.live)

}
