package es.eriktorr.pure

import cats.effect.IO
import fs2.Stream

final class FakeFetcher(data: List[TestTypes.UserData]) extends Fetcher[TestTypes.UserData]:
  override def fetch: Stream[IO, TestTypes.UserData] = Stream.emits(data)
