package ch6.state

// State action/transition
case class State[S, +A](run: S => (A, S)) {
  def flatMap[B](f: A => State[S, B]): State[S, B] = State({ s =>
    val (a, nextS) = run(s)
    f(a).run(nextS)
  })

  def map[B](f: A => B): State[S, B] = flatMap(f.andThen(State.unit))

  // flatMap[B](f: A => State[S, B]): State[S, B]
  // map[B](f: A => B): State[S, B]
  //TODO: implement map2() in terms of flatMap() and map()
  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = State({ s =>
    val (a, nextAS) = run(s)
    val (b, nextBS) = sb.run(nextAS)
    (f(a, b), nextBS)
  })

  def modify(f: S => S): State[S, Unit] = ???
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
}

//TODO: [6.12a] write flatMap x 2 + map x 1 => for comprehension
//TODO: [6.12] implement get and set