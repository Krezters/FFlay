package models.dto

import models.dao.entities.User

import java.security.MessageDigest

case class UserDTO(name: String, password: String) {
  /**
   * @return models.dao.entities.User
   */
  def toModel: User = User(0, name, passwordHash)

  /**
   * @return String hashed password
   */
  def passwordHash: String = hashPassword(password)

  /** TODO лучше если при инициализации DTO пароль будет хешироваться, но пока так
   * @param password String
   * @return String
   */
  private def hashPassword(password: String): String = {
    val md = MessageDigest.getInstance("MD5")
    val hashBytes = md.digest(password.getBytes)
    hashBytes.map("%02x".format(_)).mkString
  }
}
