package ch6.rng

import ch6.rng.rngSequence._
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngSequenceSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  // List[RNG => (A, RNG)] => RNG => (List[A], RNG)
  describe("sequence()") {
    it("combines a list of transitions into a single transition") {
      withFn[Int](sequence)(testSequenceInt)
    }
  }

  describe("sequence() with foldLeft()") {
    it("combines a list of transitions into a single transition") {
      withFn[Int](sequenceWithFoldLeft)(testSequenceInt)
    }
  }

  describe("sequence() with foldRight(), RNG.map2() and RNG.unit()") {
    it("combines a list of transitions into a single transition") {
      withFn[Int](sequenceWithFoldRightMap2)(testSequenceInt)
    }
  }

  describe("sequence() with foldLeft(), RNG.map2() and RNG.unit()") {
    it("combines a list of transitions into a single transition") {
      withFn[Int](sequenceWithFoldLeftMap2)(testSequenceInt)
    }
  }

  private def withFn[A](sequenceFnUnderTest: SequenceFn[A])(testBlock: (SequenceFn[A]) => Unit): Unit = testBlock(sequenceFnUnderTest)

  private def testSequenceInt(sequenceFn: SequenceFn[Int]): Unit = {
    val mockRng = mock[RNG]
    val nextRng1 = mock[RNG]
    val nextRng2 = mock[RNG]

    val nextIntPlusOne = RNG.map(_.nextInt)(_ + 1)
    when(mockRng.nextInt).thenReturn((1, nextRng1))
    when(nextRng1.nextInt).thenReturn((2, nextRng2))

    val (actualList, actualRng) = sequenceFn(List(RNG.int, nextIntPlusOne))(mockRng)
    actualList shouldBe List(1, 3)
    actualRng shouldBe nextRng2
    verifyNoInteractions(nextRng2)
  }
}
