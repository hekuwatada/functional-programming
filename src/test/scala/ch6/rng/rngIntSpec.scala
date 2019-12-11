package ch6.rng

import ch6.rng.rngInt._
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngIntSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]
  val newRng = mock[RNG]

  before {
    reset(mockRng, newRng)
  }

  describe("ints() - generates a list of random integers") {
    testInts(ints)
  }

  describe("intWithSequence() - generates a list of random integers") {
    testInts(intsWithSequence)
  }

  def testInts(intsFn: (Int) => (RNG) => (List[Int], RNG)): Unit = {
    it("returns empty list when size zero is given") {
      val (actualList, actualRng) = intsFn(0)(mockRng)
      actualList shouldBe empty
      actualRng shouldBe mockRng
    }

    it("returns x when size one is given") {
      when(mockRng.nextInt).thenReturn((12345, newRng))
      val (actualList, actualRng) = intsFn(1)(mockRng)
      actualList shouldBe List(12345)
      actualRng shouldBe newRng
    }

    it("returns [x,y,z] when size three is given") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]
      val nextRng3 = mock[RNG]
      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))
      when(nextRng2.nextInt).thenReturn((3, nextRng3))

      val (actualList, actualRng) = intsFn(3)(mockRng)
      actualList shouldBe List(1, 2, 3)
      actualRng shouldBe nextRng3
    }

    it("throws exception when negative size is given") {
      intercept[IllegalArgumentException] { intsFn(-1)(mockRng) }
    }
  }
}
