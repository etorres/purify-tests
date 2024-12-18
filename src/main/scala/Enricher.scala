package es.eriktorr.pure

import cats.effect.IO

trait Enricher[UserData, EnrichedUserData]:
  def enrich(data: UserData): IO[EnrichedUserData]
