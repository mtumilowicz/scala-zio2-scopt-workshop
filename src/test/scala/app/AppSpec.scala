package app

import zio.test.Assertion.{equalTo, fails}
import zio.test.TestAspect.sequential
import zio.test.{TestConsole, ZIOSpecDefault, assertTrue, assertZIO}
import zio.{Chunk, ULayer, ZIOAppArgs, ZLayer}

object AppSpec extends ZIOSpecDefault {

  val subject = App.program
  val sumCommand = Chunk("sum", "--c1", "2", "--c2", "3")
  val multiplicationCommand = Chunk("mult", "--c1", "2", "--c2", "3")
  def divisionCommand(divisor: Int) = Chunk("div", "--c1", "2", "--c2", divisor.toString)

  override def spec= suite("a")(
    test("empty args => error") {
      assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.emptyCommand)))
    }.provideSome[CommandGateway](ZIOAppArgs.empty),
    test("unknown args => error") {
      assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.notSupported)))
    }.provideSome[CommandGateway](argsLayer(Chunk("aaa"))),
    test("sum") {
      for {
        _ <- subject
        output <- TestConsole.output
      } yield assertTrue(output == Vector("5"))
    }.provideSome[CommandGateway](argsLayer(sumCommand)),
    test("multiplication") {
      for {
        _ <- subject
        output <- TestConsole.output
      } yield assertTrue(output == Vector("6"))
    }.provideSome[CommandGateway](argsLayer(multiplicationCommand)),
    test("division, divisor != 0") {
      for {
        _ <- subject
        output <- TestConsole.output
      } yield assertTrue(output == Vector("1"))
    }.provideSome[CommandGateway](argsLayer(divisionCommand(2))),
    test("division, divisor == 0") {
      assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.notSupported)))
    }.provideSome[CommandGateway](argsLayer(divisionCommand(0))),
  ).provide(CommandGateway.live, CommandService.live) @@ sequential

  private def argsLayer(chunk: Chunk[String]): ULayer[ZIOAppArgs] = ZLayer.succeed(ZIOAppArgs(chunk))
}
