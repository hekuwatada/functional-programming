package ch6.rng

import ch6.rng.RNG.Rand
import ch6.rng.rngClients.nonNegativeInt

object rngDouble {

  def double(rng: RNG): (Double, RNG) = {
    val (int, nextRng) = nonNegativeInt(rng)
    (int / (Int.MaxValue.toDouble + 1), nextRng)
  }

  def doubleWithMap(rng: RNG): (Double, RNG) = {
    val doubleRand: Rand[Double] = RNG.map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))
    doubleRand(rng)
  }
}
