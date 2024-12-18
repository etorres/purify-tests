package es.eriktorr.pure

import Storage.InMemoryStorage.StorageState

import cats.effect.{IO, Ref}

trait Storage[Precondition, EnrichedUserData, Stored]:
  def storeBatch(data: List[(Precondition, EnrichedUserData)]): IO[Stored]

object Storage:
  sealed abstract class InMemoryStorage(stateRef: Ref[IO, StorageState]):
    def store(data: List[EnrichedUserData]): IO[Unit] =
      stateRef.update(currentState => currentState.copy(currentState.data ++ data))

  object InMemoryStorage:
    final case class StorageState(data: List[EnrichedUserData])

    object StorageState:
      val empty: StorageState = StorageState(List.empty)

  private def enrichedUserDataFrom(data: List[(Unit, EnrichedUserData)]) = data.map {
    case (_, enrichedUserData) => enrichedUserData
  }

  final class Default(stateRef: Ref[IO, StorageState])
      extends InMemoryStorage(stateRef)
      with Storage[Unit, EnrichedUserData, Unit]:
    override def storeBatch(data: List[(Unit, EnrichedUserData)]): IO[Unit] =
      store(enrichedUserDataFrom(data))

  final class WithStats(stateRef: Ref[IO, StorageState])
      extends InMemoryStorage(stateRef)
      with Storage[Unit, EnrichedUserData, Stats]:
    override def storeBatch(data: List[(Unit, EnrichedUserData)]): IO[Stats] =
      val enrichedUserData = enrichedUserDataFrom(data)
      for
        _ <- store(enrichedUserData)
        stats = Stats.fromBatch(enrichedUserData)
      yield stats
