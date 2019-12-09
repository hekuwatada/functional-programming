package ch6.rng

import ch6.rng.RNG.Rand

import scala.annotation.tailrec

object rngSequence {

  // List[RNG => (A, RNG)] => RNG => (List[A], RNG)
  // combining a list of transitions into a single transition
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
    rng => recursiveSequence(List.empty[A], fs, rng)

  @tailrec
  private def recursiveSequence[A](acc: List[A], fs: List[Rand[A]], rng: RNG): (List[A], RNG) = fs match {
    case Nil => (acc, rng)
    case hsh::hst => {
      val (newA, newRng) = hsh(rng)
      recursiveSequence(acc ++ List(newA), hst, newRng)
    }
  }
}
