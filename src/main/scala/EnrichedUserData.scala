package es.eriktorr.pure

final case class EnrichedUserData(userId: UserId, registered: Boolean, friends: List[UserId])
