package app

import zio.{Chunk, ExitCode, Scope, ZIOAppArgs, ZLayer}
import zio.test.Assertion.{equalTo, fails, isFailure}
import zio.test.Spec.TestCase
import zio.test.{Spec, TestConsole, TestEnvironment, ZIOSpecDefault, assert, assertTrue}

object AppSpec extends ZIOSpecDefault {

  val subject = App.program

  override def spec= suite("a")(
    test("empty args") {
      for {
        _ <- subject
        output <- TestConsole.output
      } yield assertTrue(output == Vector("5"))
    },
  ).provideSome(CommandGateway.live, CommandService.live, ZLayer.succeed(ZIOAppArgs(Chunk("sum", "--c1", "2", "--c2", "3"))))
}
