package ch6.state

import ch6.rng.RNG
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class StateSpec extends FunSpec with Matchers with MockitoSugar with BeforeAndAfter {
  val rng = mock[RNG]
  val nextRng = mock[RNG]

  val intRng = State[RNG, Int](_.nextInt)
  val doubleRng = State[RNG, Double]({ s =>
    val (newInt, newS) = s.nextInt
    (newInt.toDouble, newS)
  })

  before {
    reset(rng, nextRng)
  }

  describe("unit()") {
    it("returns state transition as is") {
      State.unit[RNG, Int](123).run(rng) shouldBe (123, rng)
      verifyNoInteractions(rng)
    }
  }

  describe("flatMap()") {
    it("maps and flattens") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(rng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      val f: Int => State[RNG, Double] = i => State(s => {
        val (newInt, newS) = s.nextInt
        (i.toDouble + newInt, newS)
      })
      intRng.flatMap(f).run(rng) shouldBe (3.0, nextRng2)
      verifyNoInteractions(nextRng2)
    }
  }

  describe("map()") {
    it("returns State[S, B] given A => B") {
      when(rng.nextInt).thenReturn((2, nextRng))

      intRng.map(_.toString).run(rng) shouldBe ("2", nextRng)
      verifyNoInteractions(nextRng)
    }
  }

  describe("map2()") {
    it("returns State[S, C] given State[S, B] and (A, B) => C") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(rng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      intRng.map2(doubleRng)((i, d) => (i + d).toString).run(rng) shouldBe ("3.0", nextRng2)
      verifyNoInteractions(nextRng2)
    }
  }
}
