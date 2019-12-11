package ch6.rng

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  // state action that transforms state of RNG
  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  val double: Rand[Double] = map(_.nextInt)(_.toDouble)

  // passing through a state without changes; returning a constant value
  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = rng => {
    val (a, nextRng) = s(rng)
    (f(a), nextRng)
  }

  def map2[A, B, C](sa: Rand[A], sb: Rand[B])(f: (A, B) => C): Rand[C] = rng => {
    val (a, rngA) = sa(rng)
    val (b, rngB) = sb(rngA)
    (f(a, b), rngB)
  }

  //TODO: [6.8] implement flatMap
  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = ???

  //TODO: [6.8] use flatMap to implement nonNegativeLessThan

  //TODO: [6.9] Re-implement map and map2 in terms of flatMap
}
