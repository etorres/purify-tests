package es.eriktorr.pure

import cats.Monoid

final case class Stats(count: Int, registered: Int, friends: Int):
  def averageFriends: Double = friends.toDouble / count

object Stats:
  def fromBatch(data: List[EnrichedUserData]): Stats = Monoid.combineAll(data.map(fromUserData))

  private def fromUserData(data: EnrichedUserData): Stats =
    Stats(count = 1, registered = if data.registered then 1 else 0, friends = data.friends.length)

  given Monoid[Stats] = new Monoid[Stats]:
    override def empty: Stats = Stats(0, 0, 0)

    override def combine(x: Stats, y: Stats): Stats =
      Stats(x.count + y.count, x.registered + y.registered, x.friends + y.friends)
