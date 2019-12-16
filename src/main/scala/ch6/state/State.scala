package ch6.state

// State action/transition
case class State[S, +A](run: S => (A, S)) {
  def flatMap[B](f: A => State[S, B]): State[S, B] = State({ s =>
    val (a, nextS) = run(s)
    f(a).run(nextS)
  })

  def map[B](f: A => B): State[S, B] = flatMap(f.andThen(State.unit))

  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = State({ s =>
    val (a, nextAS) = run(s)
    val (b, nextBS) = sb.run(nextAS)
    (f(a, b), nextBS)
  })
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
}