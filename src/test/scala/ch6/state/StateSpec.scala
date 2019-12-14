package ch6.state

import ch6.rng.RNG
import org.mockito.Mockito.verifyNoInteractions
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class StateSpec extends FunSpec with Matchers with MockitoSugar {

  describe("unit()") {
    it("returns state transition as is") {
      val rng = mock[RNG]
      State.unit[RNG, Int](123).run(rng) shouldBe (123, rng)
      verifyNoInteractions(rng)
    }
  }
}
