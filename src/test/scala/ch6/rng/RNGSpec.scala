package ch6.rng

import ch6.rng.RNG.Rand
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class RNGSpec extends FunSpec with Matchers with MockitoSugar with BeforeAndAfter {
  val mockRng = mock[RNG]
  val mockNextRng = mock[RNG]

  before {
    reset(mockRng, mockNextRng)
  }

  describe("unit() - passes RNG state through without using it") {
    it("returns RNG => (a, RNG) given a") {
      val randA: Rand[Int] = RNG.unit(123)
      randA(mockRng) shouldBe (123, mockRng)
      verifyNoInteractions(mockRng)
    }
  }

  describe("map()") {
    it("returns RNG => (B, RNG) given RNG => (A, RNG) and A => B") {
      when(mockRng.nextInt).thenReturn((12, mockNextRng))

      val randDouble: Rand[Double] = RNG.map(RNG.int)(_.toDouble)
      val (actualDouble, actualRng) = randDouble(mockRng)

      actualDouble shouldBe 12.0
      actualRng shouldBe mockNextRng
      verifyNoInteractions(mockNextRng)
    }
  }
}
