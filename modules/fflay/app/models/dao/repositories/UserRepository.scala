package models.dao.repositories

import models.dao.schema.FFlaySchema
import models.dao.entities.User
import org.squeryl.Table

trait UserRepository {
  /**
   * @param user User
   * @return User
   */
  def insert(user: User): User

  /**
   * @param id Int
   * @return Option[User]
   */
  def get(id: Int): Option[User]

  /**
   * @param name String
   * @param password String
   * @return Option[User]
   */
  def get(name: String, password: String): Option[User]
}

class UserRepositoryImpl extends UserRepository {
  import org.squeryl.PrimitiveTypeMode._
  val users: Table[User] = FFlaySchema.users

  /**
   * @param user User
   * @return User
   */
  def insert(user: User): User = transaction(users.insert(user))

  /**
   * @param id Int
   * @return Option[User]
   */
  def get(id: Int): Option[User] = transaction {
    from(users)(u => where(u.id === id) select(u)).headOption
  }

  /**
   * @param name String
   * @param password String
   * @return Option[User]
   */
  def get(name: String, password: String): Option[User] = transaction {
    from(users)(u => where(u.name === name and u.password === password) select(u)).headOption
  }
}