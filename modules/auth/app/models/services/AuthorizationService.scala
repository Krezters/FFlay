package models.services

trait AuthorizationService {
  def check(userId: Option[String]): Boolean
}

class AuthorizationServiceImpl extends AuthorizationService{
  override def check(userId: Option[String]): Boolean = userId.nonEmpty
}