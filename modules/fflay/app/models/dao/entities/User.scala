package models.dao.entities

import org.squeryl.KeyedEntity
import org.squeryl.dsl.OneToMany
import models.dao.schema.FFlaySchema

case class User(id: Int, name: String, password: String) extends KeyedEntity[Int] {
  lazy val videos: OneToMany[Video] = FFlaySchema.userVideosRelation.left(this)
  lazy val audios: OneToMany[Audio] = FFlaySchema.userAudiosRelation.left(this)
}