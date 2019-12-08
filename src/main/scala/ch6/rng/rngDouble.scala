package ch6.rng

import ch6.rng.rngClients.nonNegativeInt

object rngDouble {

  def double(rng: RNG): (Double, RNG) = {
    val (int, nextRng) = nonNegativeInt(rng)
    (int / (Int.MaxValue.toDouble + 1), nextRng)
  }
}
