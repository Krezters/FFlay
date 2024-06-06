package models.services

import com.google.inject.Inject
import models.dao.entities.User
import models.dao.repositories.UserRepository
import models.dto.UserDTO
import scala.util.{Failure, Success, Try}

trait UserService {
  /**
   * @param user UserDTO
   * @return Try[User]
   */
  def addUser(user: UserDTO): Try[User]

  /**
   * @param user UserDTO
   * @return Try[User]
   */
  def getUser(user: UserDTO): Try[User]

  /**
   * @param id Int
   * @return Try[User]
   */
  def getUserById(id: Int): Try[User]
}

class UserServiceImpl @Inject()(
  val userRepository: UserRepository,
  val logService: LogService
) extends UserService {
  /**
   * @param user UserDTO
   * @return Try[User]
   */
  override def addUser(user: UserDTO): Try[User] = try {
    Success(userRepository.insert(user.toModel))
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }

  /**
   * @param user UserDTO
   * @return Try[User]
   */
  override def getUser(user: UserDTO): Try[User] = {
    userRepository.get(user.name, user.passwordHash) match {
      case None => Failure(new Throwable("User is not found"))
      case Some(user) => Success(user)
    }
  }

  /**
   * @param id Int
   * @return Try[User]
   */
  override def getUserById(id: Int): Try[User] = {
    userRepository.get(id) match {
      case None => Failure(new Throwable("User is not found"))
      case Some(user) => Success(user)
    }
  }
}
