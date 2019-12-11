package ch6.rng

import ch6.rng.RNG.Rand

object rngClients {
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (int, nextRng) = rng.nextInt
    (if (int < 0) -(int + 1) else int, nextRng)
  }

  def both[A, B](sa: Rand[A], sb: Rand[B]): Rand[(A, B)] =
    RNG.map2(sa, sb)((a, b) => (a, b))

  def intDouble(rng: RNG): ((Int, Double), RNG) = both(_.nextInt, nextDouble)(rng)

  def doubleInt(rng: RNG): ((Double, Int), RNG) = both(nextDouble, _.nextInt)(rng)

  private def nextDouble(rng: RNG): (Double, RNG) =
    RNG.map(_.nextInt)(_.toDouble)(rng)
}
