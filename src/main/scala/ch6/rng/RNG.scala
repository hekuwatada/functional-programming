package ch6.rng

trait RNG {
  def nextInt: (Int, RNG)
}
