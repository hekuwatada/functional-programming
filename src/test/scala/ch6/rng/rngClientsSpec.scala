package ch6.rng

import ch6.rng.rngClients._
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngClientsSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]
  val newRng = mock[RNG]

  before {
    reset(mockRng)
  }

  describe("nonNegativeInt() - generates a random integer between 0 and Int.maxValue") {
    it("returns a zero when RNG returns a zero") {
      when(mockRng.nextInt).thenReturn((0, newRng))
      nonNegativeInt(mockRng) shouldBe (0, newRng)
    }

    it("return a positive integer when RNG returns a positive random integer") {
      when(mockRng.nextInt).thenReturn((1234, newRng))
      nonNegativeInt(mockRng) shouldBe (1234, newRng)
    }

    it("return a positive integer when RNG returns a negative random integer") {
      when(mockRng.nextInt).thenReturn((-1234, newRng))
      nonNegativeInt(mockRng) shouldBe (1233, newRng)
    }

    it("return a positive integer when RNG returns -1") {
      when(mockRng.nextInt).thenReturn((-1, newRng))
      nonNegativeInt(mockRng) shouldBe (0, newRng)
    }

    it("return a positive integer when RNG returns min negative integer") {
      when(mockRng.nextInt).thenReturn((Int.MinValue, newRng))
      nonNegativeInt(mockRng) shouldBe (Int.MaxValue, newRng)
    }
  }

  describe("double() - generates a Double between 0 and 1 (exclusive)") {
    it("returns a zero when RNG returns a zero") {
      when(mockRng.nextInt).thenReturn((0, newRng))
      double(mockRng) shouldBe (0.0, newRng)
    }

    it("returns a zero int when RNG returns -1") {
      when(mockRng.nextInt).thenReturn((-1, newRng))
      double(mockRng) shouldBe (0.0, newRng)
    }

    it("returns 0 < y < 1 when RNG returns a random negative integer x") {
      when(mockRng.nextInt).thenReturn((-123456, newRng))
      val (actualInt, actualRng) = double(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }

    it("returns 0 < y < 1 when RNG returns min integer") {
      when(mockRng.nextInt).thenReturn((Int.MinValue, newRng))
      val (actualInt, actualRng) = double(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }

    it("returns 0 < y < 1 when RNG returns max integer") {
      when(mockRng.nextInt).thenReturn((Int.MaxValue, newRng))
      val (actualInt, actualRng) = double(mockRng)
      actualRng shouldBe newRng
      actualInt should be > 0.0
      actualInt should be < 1.0
    }
  }
}
