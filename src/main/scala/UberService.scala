package es.eriktorr.pure

import cats.Monoid
import cats.effect.IO

import scala.concurrent.duration.DurationInt

final class UberService[UserData, EnrichedUserData, Bookkept, Stored: Monoid](
    fetcher: Fetcher[UserData],
    enricher: Enricher[UserData, EnrichedUserData],
    bookkeeper: Bookkeeper[Bookkept, UserData, EnrichedUserData],
    storage: Storage[Bookkept, EnrichedUserData, Stored],
):
  def fetchAndStore: IO[Stored] =
    fetcher.fetch
      .evalMap(userData =>
        for
          enrichedUserData <- enricher.enrich(userData)
          bookkept <- bookkeeper.bookkeep(userData, enrichedUserData)
        yield bookkept -> enrichedUserData,
      )
      .groupWithin(5, 10.seconds)
      .parEvalMap(UberService.cores)(chunk => storage.storeBatch(chunk.toList))
      .compile
      .foldMonoid

object UberService:
  private def cores = scala.math.min(1, Runtime.getRuntime.nn.availableProcessors().nn / 2)
