package ch6.state

// State action/transition
case class State[S, +A](run: S => (A, S)) {
  def flatMap[B](f: A => State[S, B]): State[S, B] = State({ s =>
    val (a, nextS) = run(s)
    f(a).run(nextS)
  })

  // TODO: implement map, map2, flatMap
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
}