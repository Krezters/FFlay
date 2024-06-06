package models.dao.schema

import models.dao.entities.{Audio, User, Video}
import org.squeryl.{PrimitiveTypeMode, Schema, Table}

object FFlaySchema extends Schema {
  import org.squeryl.PrimitiveTypeMode._

  val users: Table[User] = table[User]("users")
  val videos: Table[Video] = table[Video]("videos")
  val audios: Table[Audio] = table[Audio]("audios")

  on(users)(u => declare(u.id is autoIncremented("users_id_seq")))
  on(videos)(v => declare(v.id is autoIncremented("videos_id_seq")))
  on(audios)(a => declare(a.id is autoIncremented("audios_id_seq")))

  val userVideosRelation: PrimitiveTypeMode.OneToManyRelationImpl[User, Video] =
    oneToManyRelation(users, videos).via(_.id === _.userId)

  val userAudiosRelation: PrimitiveTypeMode.OneToManyRelationImpl[User, Audio] =
    oneToManyRelation(users, audios).via(_.id === _.userId)
}