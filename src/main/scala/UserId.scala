package es.eriktorr.pure

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Positive0

opaque type UserId <: Int :| Positive0 = Int :| Positive0

object UserId extends RefinedTypeOps[Int, Positive0, UserId]
