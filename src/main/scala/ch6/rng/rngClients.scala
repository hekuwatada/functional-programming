package ch6.rng

import ch6.rng.RNG.Rand

import scala.annotation.tailrec

object rngClients {
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (int, nextRng) = rng.nextInt
    (if (int < 0) -(int + 1) else int, nextRng)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    require(count >= 0)
    recursiveInts(count, rng, List.empty[Int])
  }

  @tailrec
  private def recursiveInts(count: Int, rng: RNG, acc: List[Int]): (List[Int], RNG) = {
    if (acc.size == count) (acc, rng)
    else {
      val (newInt, newRng) = rng.nextInt
      recursiveInts(count, newRng, acc ++ List(newInt)) // if order does not matter, do newInt :: acc
    }
  }

  def both[A, B](sa: Rand[A], sb: Rand[B]): Rand[(A, B)] =
    RNG.map2(sa, sb)((a, b) => (a, b))

  def intDouble(rng: RNG): ((Int, Double), RNG) = both(_.nextInt, nextDouble)(rng)

  def doubleInt(rng: RNG): ((Double, Int), RNG) = both(nextDouble, _.nextInt)(rng)

  private def nextDouble(rng: RNG): (Double, RNG) =
    RNG.map(_.nextInt)(_.toDouble)(rng)
}
