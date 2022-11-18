package app.infrastructure.scoptreaders

import app.domain.CardinalDirection

object CardinalDirectionReader {

  implicit val cardinalDirectionRead: scopt.Read[CardinalDirection] =
    scopt.Read.reads(CardinalDirection withNameInsensitive _)

}
