package es.eriktorr.pure

import Storage.InMemoryStorage.StorageState
import StorageSuite.testCaseGen

import cats.effect.{IO, Ref}
import cats.implicits.toTraverseOps
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Gen
import org.scalacheck.cats.implicits.given
import org.scalacheck.effect.PropF.forAllF

final class StorageSuite extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("should store a list of elements"):
    forAllF(testCaseGen): testCase =>
      (for
        stateRef <- Ref.of[IO, StorageState](StorageState.empty)
        storage = Storage.Default(stateRef)
        _ <- storage.storeBatch(testCase.data)
        finalState <- stateRef.get
      yield finalState.data).assertEquals(testCase.expectedData)

  test("should store a list of elements and compute stats"):
    forAllF(testCaseGen): testCase =>
      (for
        stateRef <- Ref.of[IO, StorageState](StorageState.empty)
        storage = Storage.WithStats(stateRef)
        stats <- storage.storeBatch(testCase.data)
        finalState <- stateRef.get
      yield finalState.data -> stats).map: (obtainedData, obtainedStats) =>
        assertEquals(obtainedData, testCase.expectedData)
        assertEquals(obtainedStats, testCase.expectedStats)

object StorageSuite:
  final private case class TestCase(
      data: List[(Unit, EnrichedUserData)],
      expectedData: List[EnrichedUserData],
      expectedStats: Stats,
  )

  private val idGen = Gen.choose(1, 1_000_000)

  private val userIdGen = idGen.map(UserId.applyUnsafe)

  private val testCaseGen = for
    size <- Gen.choose(1, 3)
    ids <- Gen.containerOfN[Set, Int](size, idGen)
    data <- ids.toList.traverse(id =>
      for
        registered <- Gen.oneOf(true, false)
        userId = UserId.applyUnsafe(id)
        friends <- Gen.containerOf[List, UserId](userIdGen)
      yield () -> EnrichedUserData(userId, registered, friends),
    )
    expectedData = data.map { case (_, enrichedUserData) => enrichedUserData }
    expectedStats = Stats.fromBatch(expectedData)
  yield TestCase(data, expectedData, expectedStats)
