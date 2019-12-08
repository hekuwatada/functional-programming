package ch6.rng

import ch6.rng.rngClients.nonNegativeInt
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class rngClientsSpec extends FunSpec with Matchers with BeforeAndAfter with MockitoSugar {

  val mockRng = mock[RNG]
  val newRng = mock[RNG]

  before {
    reset(mockRng)
  }

  describe("nonNegativeInt()") {
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
}
