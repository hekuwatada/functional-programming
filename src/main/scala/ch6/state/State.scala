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

  def map2WithFlatMapMap[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    flatMap({ a => sb.map(b => f(a, b)) })

  def modify(f: S => S): State[S, Unit] = ???
}

object State {
  type Map2Fn[A, B, C, S] = State[S, B] => ((A, B) => C) => State[S, C]

  type SequenceFn[A, S] = List[State[S, A]] => State[S, List[A]]

  def unit[S, A](a: A): State[S, A] = State(s => (a, s))

  def sequence[S, A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.foldRight(unit[S, List[A]](List.empty[A]))((acc, sa) => acc.map2(sa)(_ :: _))
}

//TODO: [6.12a] write flatMap x 2 + map x 1 => for comprehension
//TODO: [6.12] implement get and set