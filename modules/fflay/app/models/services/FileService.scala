package models.services

import com.google.inject.Inject
import models.dto.FileDTO

import java.io._
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

trait FileService {
  def save(file: FileDTO): Try[String]
  def getData(name: String): Try[String]
  def delete(file: FileDTO): Boolean
}

class FileServiceImpl @Inject()(val logService: LogService) extends FileService {
  val path: String = getClass.getResource("").getPath

  override def save(fileDTO: FileDTO): Try[String] = {
    val file: File = new File(path + fileDTO.name)
    val writer = new PrintWriter(file)
    val source: BufferedWriter = new BufferedWriter(writer)
    try {
      source.write(fileDTO.data)
      Success(file.getPath)
    } catch {
      case e: Throwable => logService.log(e.getMessage)
        Failure(new Throwable("An error occurred, please try later"))
    } finally {
      source.close()
    }
  }

  override def getData(name: String): Try[String] = {
    val file: File = new File(path + name)
    val source: BufferedSource = Source.fromFile(file)

    val lines: Try[String] = try {
      Success(source.getLines().mkString)
    } catch {
      case e: Throwable => logService.log(e.getMessage)
        Failure(new Throwable("An error occurred, please try later"))
    } finally {
      source.close()
    }

    lines
  }

  override def delete(fileDTO: FileDTO): Boolean = {
    val file = new File(path + fileDTO.name)
    if (file.exists()) file.delete() else true
  }
}
