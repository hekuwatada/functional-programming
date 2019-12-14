## Category
* a collection of objects
* a collection of arrows
* composition of arrows (TBC)
* requires: identity, associativity (TBC)

## Monad
Monad is useful in such a way that its operations can be chained and expressed as `for comprehension`.
* `identity` (unit/pure/return/zero/effectively apply) - wrapping/monad construction
* `bind` (flatMap) - can build `map()`

`flatMap()` can be decomposed as `map()` and `flatten()`, therefore below form a Monad:
* `def unit = A => F`
* `def map = F[A] => (A => B) => F[B]`
* `def flatten = F[F[A]] => F[A]`

### Monad laws
* identity
* associativity

## Functor

## Applicative

## Monoid

## Notes
* `referential transparency` - it may be replaced by its value