package app.infrastructure.scoptreaders

import app.domain.Types.{NonZero, NonZeroInt}
import eu.timepit.refined.refineV

object RefinedReader {

  implicit val nonZeroRead: scopt.Read[NonZeroInt] =
    scopt.Read.intRead.map(unsafeParseInt)

  private def unsafeParseInt(i: Int): NonZeroInt = refineV[NonZero](i)
    .fold(_ => throw new IllegalArgumentException("'" + i + "' cannot be zero."), identity)

}
