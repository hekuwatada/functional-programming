package ch6.rng

import ch6.rng.rngSequence.sequence
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngSequenceSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]

  before {
    reset(mockRng)
  }

  // List[RNG => (A, RNG)] => RNG => (List[A], RNG)
  describe("sequence()") {
    it("combines a list of transitions into a single transition") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      val nextIntPlusOne = RNG.map(_.nextInt)(_ + 1)
      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      val (actualList, actualRng) = sequence(List(RNG.int, nextIntPlusOne))(mockRng)
      actualList shouldBe List(1, 3)
      actualRng shouldBe nextRng2
      verifyNoInteractions(nextRng2)
    }
  }
}
