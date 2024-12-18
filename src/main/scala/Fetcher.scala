package es.eriktorr.pure

import cats.effect.IO
import fs2.Stream

trait Fetcher[UserData]:
  def fetch: Stream[IO, UserData]
