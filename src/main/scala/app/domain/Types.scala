package app.domain

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all._

object Types {

  type NonZero = Positive Or Negative
  type NonZeroInt = Int Refined NonZero

}
