package ch6.rng

object rngClients {
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (int, nextRng) = rng.nextInt
    (if (int < 0) -(int + 1) else int, nextRng)
  }
}
