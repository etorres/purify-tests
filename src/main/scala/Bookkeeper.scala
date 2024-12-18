package es.eriktorr.pure

import cats.effect.IO

trait Bookkeeper[Bookkept, UserData, EnrichedUserData]:
  def bookkeep(original: UserData, enriched: EnrichedUserData): IO[Bookkept]

object Bookkeeper:
  final class Default extends Bookkeeper[Unit, UserData, EnrichedUserData]:
    override def bookkeep(original: UserData, enriched: EnrichedUserData): IO[Unit] = IO.unit
