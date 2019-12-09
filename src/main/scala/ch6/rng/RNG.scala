package ch6.rng

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, nextRng) = s(rng)
      (f(a), nextRng)
    }

  def map2[A, B, C](sa: Rand[A], sb: Rand[B])(f: (A, B) => C): Rand[C] = rng => {
    val (a, rngA) = sa(rng)
    val (b, rngB) = sb(rngA)
    (f(a, b), rngB)
  }
}
