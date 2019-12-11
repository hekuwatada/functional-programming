package ch6.rng

import ch6.rng.RNG.Rand

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

  // def sequenceWithFoldRightMap2[A](fs: List[Rand[A]]): Rand[List[A]]
  // use List.fill(n)(x) -- make a list with x repeated n times
  // type Rand[+A] = RNG => (A, RNG)
  def intsWithSequence(count: Int)(rng: RNG): (List[Int], RNG) = {
    require(count >= 0)
    val randsInt: List[Rand[Int]] = List.fill(count)(RNG.int)
    val randIntList: Rand[List[Int]] = rngSequence.sequenceWithFoldRightMap2(randsInt)
    randIntList(rng)
  }
}
