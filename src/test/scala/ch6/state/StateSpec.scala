package ch6.state

import ch6.rng.RNG
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class StateSpec extends FunSpec with Matchers with MockitoSugar with BeforeAndAfter {
  val rng = mock[RNG]

  before {
    reset(rng)
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

      val state = State[RNG, Int](_.nextInt)
      val f: Int => State[RNG, Double] = i => State(s => {
        val (newInt, newS) = s.nextInt
        (i.toDouble + newInt, newS)
      })
      state.flatMap(f).run(rng) shouldBe (3.0, nextRng2)
      verifyNoInteractions(nextRng2)
    }
  }
}
