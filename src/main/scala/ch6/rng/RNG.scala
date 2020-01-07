package ch6.rng

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  // state action that transforms state of RNG
  //TODO: make Rand[A] type alias to State[A, RNG]
  type Rand[+A] = RNG => (A, RNG)

  type MapFn[A, B] = Rand[A] => (A => B) => Rand[B] //TODO: type variance

  val int: Rand[Int] = _.nextInt

  val double: Rand[Double] = map(_.nextInt)(_.toDouble)

  // passing through a state without changes; returning a constant value
  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = rng => {
    val (a, nextRng) = s(rng)
    (f(a), nextRng)
  }

  // flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B]
  def mapWithFlatMap[A, B](s: Rand[A])(f: A => B): Rand[B] = flatMap(s)(a => RNG.unit(f(a)))

  def map2[A, B, C](sa: Rand[A], sb: Rand[B])(f: (A, B) => C): Rand[C] = rng => {
    val (a, rngA) = sa(rng)
    val (b, rngB) = sb(rngA)
    (f(a, b), rngB)
  }

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = rng => {
    val (a, nextRng) = f(rng)
    g(a)(nextRng)
  }


  //TODO: [6.8] use flatMap to implement nonNegativeLessThan

  //TODO: [6.9] Re-implement map and map2 in terms of flatMap

  //TODO: EXERCISE 4: Write a function to generate a list of random integers.
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = ???
}
