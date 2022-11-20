package app

import app.domain.{CardinalDirection, CommandExecutionError, CommandService}
import app.gateway.CommandGateway
import zio.test.Assertion.{equalTo, fails, isGreaterThanEqualTo, isLessThan}
import zio.test.{TestConsole, ZIOSpecDefault, assert, assertTrue, assertZIO}
import zio.{Chunk, ZIOAppArgs, ZLayer}

object AppSpec extends ZIOSpecDefault {

  val subject = App.program
  val sumCommand = Chunk("sum", "--constituent1", "2", "--constituent2", "3")
  val multiplicationCommand = Chunk("multiplication", "--constituent1", "2", "--constituent2", "3")
  val goEastCommand = Chunk("go", "--direction", CardinalDirection.East.entryName)
  val randomPositive = Chunk("random", "--positive")
  val randomNegative = Chunk("random", "--negative")
  val randomDefault = Chunk("random")
  val help = Chunk("--help")
  private def divisionCommand(divisor: Int) = Chunk("divide", "--dividend", "2", "--divisor", divisor.toString)

  override def spec = suite("app tests")(
    emptyArgsError,
    unknownArgsError,
    sumSuccess,
    multiplicationSuccess,
    divisionNonZeroDivisorSuccess,
    divisionZeroDivisorError,
    goDirectionEastSuccess,
    randomPositiveSuccess,
    randomNegativeSuccess,
    randomDefaultSuccess,
    helpSuccess
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

  lazy val goDirectionEastSuccess = test("go direction East") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assertTrue(result == "going into East")
  }.provideSome[CommandGateway](argsLayer(goEastCommand))

  lazy val randomPositiveSuccess = test("draw random positive int") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assert(result.toInt)(isGreaterThanEqualTo(0))
  }.provideSome[CommandGateway](argsLayer(randomPositive))

  lazy val randomNegativeSuccess = test("draw random positive int") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assert(result.toInt)(isLessThan(0))
  }.provideSome[CommandGateway](argsLayer(randomNegative))

  lazy val randomDefaultSuccess = test("default means random positive") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assert(result.toInt)(isGreaterThanEqualTo(0))
  }.provideSome[CommandGateway](argsLayer(randomDefault))

  lazy val helpSuccess = test("help") {
    for {
      _ <- subject
      result <- firstOutput
    } yield assertTrue(result.nonEmpty)
  }.provideSome[CommandGateway](argsLayer(help))


  private def argsLayer(chunk: Chunk[String]) = ZLayer.succeed(ZIOAppArgs(chunk))

  private val firstOutput = TestConsole.output.map(_.head).map(_.trim)
}
