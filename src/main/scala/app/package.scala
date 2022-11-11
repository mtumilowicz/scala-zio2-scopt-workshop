import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all._

package object app {

  type NonZero = Positive Or Negative
  type NonZeroInt = Int Refined NonZero

}
