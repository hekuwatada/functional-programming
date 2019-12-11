package ch6.rng

import scala.annotation.tailrec

object rngInt {

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
}
