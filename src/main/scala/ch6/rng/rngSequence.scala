package ch6.rng

import ch6.rng.RNG.Rand

import scala.annotation.tailrec

object rngSequence {

  type SequenceFn[A] = List[Rand[A]] => Rand[List[A]]

  // List[RNG => (A, RNG)] => RNG => (List[A], RNG)
  // combining a list of transitions into a single transition
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
    rng => recursiveSequence(RNG.unit(List.empty[A])(rng), fs)

  @tailrec
  private def recursiveSequence[A](acc: (List[A], RNG), fs: List[Rand[A]]): (List[A], RNG) = fs match {
    case Nil => acc
    case randA::tail =>
      val (accListA, accRng) = acc
      val (newA, newRng) = randA(accRng)
      recursiveSequence((accListA ++ List(newA), newRng), tail)
  }

  // foldRight[B](z: B)(op: (A, B) => B): B
  // B = (List[A], RNG)
  // A = RNG => (A, RNG)
  def sequenceWithFoldRight[A](fs: List[Rand[A]]): Rand[List[A]] = {
    rng => fs.foldRight(RNG.unit(List.empty[A])(rng))((randA: Rand[A], acc: (List[A], RNG)) => {
      val (accListA: List[A], accRng: RNG) = acc
      val (newA: A, newRng: RNG) = randA(accRng)
      (accListA ++ List(newA), newRng)
    })
  }
}
