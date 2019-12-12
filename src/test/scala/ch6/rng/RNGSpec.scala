package ch6.rng

import ch6.rng.RNG.{MapFn, Rand}
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
    withMap[Int, Double](RNG.map)(testMapIntDouble)
  }

  describe("map2()") {
    testMap2(RNG.map2)
  }

  // flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B]
  describe("flatMap()") {
    it("returns Rand[B] given Rand[A] and A => Rand[B] where B is Double and A is Int") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((5, nextRng2))

      val randDouble = RNG.flatMap(RNG.int)(_ => RNG.double)

      randDouble(mockRng) shouldBe (5.0, nextRng2)
      verifyNoInteractions(nextRng2)
    }
  }

  private def withMap[A, B](mapFn: MapFn[A, B])(testBlock: MapFn[A, B] => Unit): Unit =
    testBlock(mapFn)

  private def testMapIntDouble(mapFn: MapFn[Int, Double]): Unit = {
    it("returns RNG => (B, RNG) given RNG => (A, RNG) and A => B") {
      when(mockRng.nextInt).thenReturn((12, mockNextRng))

      val randDouble: Rand[Double] = mapFn(RNG.int)(_.toDouble)
      val (actualDouble, actualRng) = randDouble(mockRng)

      actualDouble shouldBe 12.0
      actualRng shouldBe mockNextRng
      verifyNoInteractions(mockNextRng)
    }
  }

  private def testMap2[A, B, C](map2Fn: (Rand[A], Rand[B]) => ((A, B) => C) => Rand[C]): Unit = {
    it("returns RNG => (c, RNG) given RNG => (a, RNG), RNG => (b, RNG) and (a, b) => c") {
      val nextRng1 = mock[RNG]
      val nextRng2 = mock[RNG]

      when(mockRng.nextInt).thenReturn((1, nextRng1))
      when(nextRng1.nextInt).thenReturn((2, nextRng2))

      val randC: Rand[Int] = RNG.map2(_.nextInt, _.nextInt)(_ + _)
      randC(mockRng) shouldBe (3, nextRng2)
      verifyNoInteractions(nextRng2)
    }
  }
}
