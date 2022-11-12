package app

import zio.test.Assertion.{equalTo, fails}
import zio.test.{TestConsole, ZIOSpecDefault, assertTrue, assertZIO}
import zio.{Chunk, ZIOAppArgs, ZLayer}

object AppSpec extends ZIOSpecDefault {

  val subject = App.program
  val sumCommand = Chunk("sum", "--c1", "2", "--c2", "3")
  val multiplicationCommand = Chunk("mult", "--c1", "2", "--c2", "3")
  private def divisionCommand(divisor: Int) = Chunk("div", "--c1", "2", "--c2", divisor.toString)

  override def spec = suite("a")(
    emptyArgsError,
    unknownArgsError,
    sumSuccess,
    multiplicationSuccess,
    divisionNonZeroDivisorSuccess,
    divisionZeroDivisorError,
  ).provideSome(CommandGateway.live, CommandService.live)

  lazy val emptyArgsError = test("empty args => error") {
    assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.emptyCommand)))
  }.provideSome[CommandGateway](ZIOAppArgs.empty)

  lazy val unknownArgsError = test("unknown args => error") {
    assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.notSupported)))
  }.provideSome[CommandGateway](argsLayer(Chunk("aaa")))

  lazy val sumSuccess = test("sum => success") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assertTrue(result == "5")
  }.provideSome[CommandGateway](argsLayer(sumCommand))

  lazy val multiplicationSuccess = test("multiplication => success") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assertTrue(result == "6")
  }.provideSome[CommandGateway](argsLayer(multiplicationCommand))

  lazy val divisionNonZeroDivisorSuccess = test("division, divisor != 0 => success") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assertTrue(result == "1")
  }.provideSome[CommandGateway](argsLayer(divisionCommand(2)))

  lazy val divisionZeroDivisorError = test("division, divisor == 0 => error") {
    assertZIO(subject.exit)(fails(equalTo(CommandExecutionError.notSupported)))
  }.provideSome[CommandGateway](argsLayer(divisionCommand(0)))

  private def argsLayer(chunk: Chunk[String]) = ZLayer.succeed(ZIOAppArgs(chunk))

  private val firstOutput = TestConsole.output.map(_.head).map(_.trim)
}
