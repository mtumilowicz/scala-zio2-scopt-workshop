package app.domain

import zio.ZIO

case class CommandExecutionError(message: String)

object CommandExecutionError {
  def fromError(exception: Exception) = CommandExecutionError(exception.getLocalizedMessage)
  def zio(message: String) = ZIO.fail(CommandExecutionError(message))

  val emptyCommand = CommandExecutionError("parsing error: empty command not supported")

  def notSupported(command: String) = CommandExecutionError(s"parsing error: command $command not supported")
}
