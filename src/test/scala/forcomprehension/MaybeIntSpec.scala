package forcomprehension

import org.scalatest.{FunSpec, Matchers}

class MaybeIntSpec extends FunSpec with Matchers {

  describe("flatMap() and map()") {
    it("unboxes MaybeInt") {
      val maybe = SomeInt(5)
      val no = NoInt

      val actual = maybe.flatMap(a => no.map { b => a + b })

      actual shouldBe NoInt
    }

    it("unboxes SomeInt") {
      val maybe1 = SomeInt(2)
      val maybe2 = SomeInt(3)
      val maybe3 = SomeInt(5)

      val actual = maybe1.flatMap(a => maybe2.flatMap(b => maybe3.map(c => a + b + c)))

      actual shouldBe SomeInt(10)
    }
  }

  describe("for comprehension") {
    it("unboxes MaybeInt in for comprehension") {
      val maybe = SomeInt(5)
      val no = NoInt

      val actual = for {
        a <- maybe
        b <- { a shouldBe 5; no }
      } yield a + b

      actual shouldBe NoInt
    }

    it("unboxes SomeInt in for comprehension") {
      val actual = for {
        a <- SomeInt(2)
        b <- SomeInt(3)
      } yield a + b

      actual shouldBe SomeInt(5)
    }
  }
}
