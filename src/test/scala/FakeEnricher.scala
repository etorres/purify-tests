package es.eriktorr.pure

import cats.effect.IO

object FakeEnricher extends Enricher[TestTypes.UserData, TestTypes.EnrichedUserData]:
  override def enrich(data: TestTypes.UserData): IO[TestTypes.EnrichedUserData] =
    IO.pure(s"enriched($data)")
