package es.eriktorr.pure

import munit.CatsEffectSuite

final class UberServiceSuite extends CatsEffectSuite:
  test("should fetch the user data, enrich it, and store the results"):
    val userData = (1 to 12).toList.map(x => s"data$x")
    val expected = userData.map(x => s"stored(bookkept($x,enriched($x)),enriched($x))")
    UberService(
      FakeFetcher(userData),
      FakeEnricher,
      FakeBookkeeper,
      FakeStorage,
    ).fetchAndStore.assertEquals(expected)
