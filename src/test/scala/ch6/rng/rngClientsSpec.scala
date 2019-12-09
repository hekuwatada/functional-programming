package ch6.rng

import ch6.rng.rngClients._
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngClientsSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]
  val newRng = mock[RNG]

  before {
    reset(mockRng, newRng)
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

  describe("ints() - generates a list of random integers") {
    it("returns empty list when size zero is given") {
      val (actualList, actualRng) = ints(0)(mockRng)
      actualList shouldBe empty
      actualRng shouldBe mockRng
    }

    it("returns x when size one is given") {
      when(mockRng.nextInt).thenReturn((12345, newRng))
      val (actualList, actualRng) = ints(1)(mockRng)
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

      val (actualList, actualRng) = ints(3)(mockRng)
      actualList shouldBe List(1, 2, 3)
      actualRng shouldBe nextRng3
    }

    it("throws exception when negative size is given") {
      intercept[IllegalArgumentException] { ints(-1)(mockRng) }
    }
  }

  describe("combinations") {
    it("intDouble() generates ((Int, Double), RNG)") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      val ((actualInt, actualDouble), actualRng) = intDouble(mockRng)
      actualInt shouldBe 1
      actualDouble shouldBe 2.0
      actualRng shouldBe nextRng2
      verifyNoInteractions(nextRng2)
    }

    it("doubleInt() generates ((Double, Int), RNG)") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      val ((actualDouble, actualInt), actualRng) = doubleInt(mockRng)
      actualInt shouldBe 2
      actualDouble shouldBe 1.0
      actualRng shouldBe nextRng2
      verifyNoInteractions(nextRng2)
    }
  }
}
