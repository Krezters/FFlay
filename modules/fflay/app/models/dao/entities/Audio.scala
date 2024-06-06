package models.dao.entities

import org.squeryl.KeyedEntity

case class Audio(id: Int, name: String, link: String, ready: Boolean, userId: Int) extends KeyedEntity[Int]
