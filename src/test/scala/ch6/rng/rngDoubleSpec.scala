package ch6.rng

import ch6.rng.rngDouble._
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngDoubleSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]
  val newRng = mock[RNG]

  before {
    reset(mockRng)
  }

  describe("double() - generates a Double between 0 and 1 (exclusive)") {
    testDouble(double)
  }

  private def testDouble(doubleFnUnderTest: (RNG) => (Double, RNG)): Unit = {
    it("returns a zero when RNG returns a zero") {
      when(mockRng.nextInt).thenReturn((0, newRng))
      doubleFnUnderTest(mockRng) shouldBe (0.0, newRng)
    }

    it("returns a zero int when RNG returns -1") {
      when(mockRng.nextInt).thenReturn((-1, newRng))
      doubleFnUnderTest(mockRng) shouldBe (0.0, newRng)
    }

    it("returns 0 < y < 1 when RNG returns a random negative integer x") {
      when(mockRng.nextInt).thenReturn((-123456, newRng))
      val (actualInt, actualRng) = doubleFnUnderTest(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }

    it("returns 0 < y < 1 when RNG returns min integer") {
      when(mockRng.nextInt).thenReturn((Int.MinValue, newRng))
      val (actualInt, actualRng) = doubleFnUnderTest(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }

    it("returns 0 < y < 1 when RNG returns max integer") {
      when(mockRng.nextInt).thenReturn((Int.MaxValue, newRng))
      val (actualInt, actualRng) = doubleFnUnderTest(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }
  }
}
