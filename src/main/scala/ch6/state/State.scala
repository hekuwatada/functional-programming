package ch6.state

// State action/transition
case class State[S, +A](run: S => (A, S)) {

  // TODO: implement map, map2, flatMap
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
}