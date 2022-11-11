package app

import zio.ZIO

case class CommandExecutionError(message: String)

object CommandExecutionError {
  def fromError(exception: Exception) = CommandExecutionError(exception.getLocalizedMessage)
  def zio(message: String) = ZIO.fail(CommandExecutionError(message))
}
