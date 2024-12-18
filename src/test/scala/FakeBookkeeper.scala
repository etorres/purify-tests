package es.eriktorr.pure

import cats.effect.IO

object FakeBookkeeper
    extends Bookkeeper[TestTypes.Bookkept, TestTypes.UserData, TestTypes.EnrichedUserData]:
  override def bookkeep(
      original: TestTypes.UserData,
      enriched: TestTypes.EnrichedUserData,
  ): IO[TestTypes.Bookkept] = IO.pure(s"bookkept($original,$enriched)")
