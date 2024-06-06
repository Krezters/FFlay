package models.services

trait LogService {
  def log(str: String): Unit
}

class LogServiceImpl extends LogService{
  override def log(str: String): Unit = println(str)
}
