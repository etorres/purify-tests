package es.eriktorr.pure

import cats.effect.IO

object FakeStorage
    extends Storage[TestTypes.Bookkept, TestTypes.EnrichedUserData, TestTypes.Stored]:
  override def storeBatch(
      data: List[(TestTypes.Bookkept, TestTypes.EnrichedUserData)],
  ): IO[TestTypes.Stored] =
    IO.pure(data.map { case (bookkept, enriched) => s"stored($bookkept,$enriched)" })
