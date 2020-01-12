package ch6.state

import ch6.rng.RNG.Rand
import ch6.rng.{RNG, rngInt, rngSequence}
import ch6.state.State.Map2Fn
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

  def ints(count: Int): State[RNG, List[Int]] = {
    require(count >= 0)
    val randsInt: List[State[RNG, Int]] = List.fill(count)(intRng)
    State.sequence(randsInt)
  }

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
      testMap2WithRngDoublePlusString(intRng.map2)
    }
  }

  describe("map2WithFlatMapMap()") {
    it("returns State[S, C] given State[S, B] and (A, B) => C") {
      testMap2WithRngDoublePlusString(intRng.map2WithFlatMapMap)
    }
  }

  describe("sequence()") {
    it("combines a list of transitions into a single transition") {
      testSequenceInt(State.sequence)
    }
  }

  describe("for comprehension") {
    it("returns a list of integers - flatMap & map") {
      val stateAction: State[RNG, List[Int]] = intRng.flatMap { x =>
        intRng.flatMap { y =>
          ints(x).map { xs =>
            xs.map { z =>
              z % y
            }
          }
        }
      }

      testGeneratingIntegers(stateAction)
    }

    it("returns a list of integers - for comprehension") {
      val stateAction: State[RNG, List[Int]] = for {
        x <- intRng
        y <- intRng
        xs <- ints(x)
      } yield {
        xs.map(_ % y)
      }

      testGeneratingIntegers(stateAction)
    }
  }

  private def testGeneratingIntegers(stateActionUnderTest: State[RNG, List[Int]]): Unit = {
    val nextRng1 = mock[RNG]
    val nextRng2 = mock[RNG]
    val nextRng3 = mock[RNG]
    val nextRng4 = mock[RNG]

    when(rng.nextInt).thenReturn((2, nextRng1))
    when(nextRng1.nextInt).thenReturn((10, nextRng2))
    when(nextRng2.nextInt).thenReturn((15, nextRng3))
    when(nextRng3.nextInt).thenReturn((7, nextRng4))

    val (actualInts, actualRng) = stateActionUnderTest.run(rng)

    actualRng shouldBe nextRng4
    actualInts shouldBe List(5, 7)
    verifyNoInteractions(nextRng4)
  }
  //  type Map2Fn[A, B, C, S] = State[S, B] => ((A, B) => C) => State[S, C]
  private def testMap2WithRngDoublePlusString(map2Fn: Map2Fn[Int, Double, String, RNG]) {
    val nextRng1 = mock[RNG]
    val nextRng2 = mock[RNG]

    when(rng.nextInt).thenReturn((1, nextRng1))
    when(nextRng1.nextInt).thenReturn((2, nextRng2))

    map2Fn(doubleRng)((i, d) => (i + d).toString).run(rng) shouldBe ("3.0", nextRng2)
    verifyNoInteractions(nextRng2)
  }

  private def testSequenceInt(sequenceFn: State.SequenceFn[Int, RNG]): Unit = {
    val mockRng = mock[RNG]
    val nextRng1 = mock[RNG]
    val nextRng2 = mock[RNG]
    val nextRng3 = mock[RNG]

    val nextIntPlusOne: State[RNG, Int] = intRng.map(_ + 1)
    when(mockRng.nextInt).thenReturn((1, nextRng1))
    when(nextRng1.nextInt).thenReturn((2, nextRng2))
    when(nextRng2.nextInt).thenReturn((9, nextRng3))

    val (actualList, actualRng) = sequenceFn(List(intRng, nextIntPlusOne, intRng)).run(mockRng)
    actualList shouldBe List(1, 3, 9)
    actualRng shouldBe nextRng3
    verifyNoInteractions(nextRng3)
  }
}
