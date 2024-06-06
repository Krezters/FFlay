package models.services

import com.google.inject.Inject
import models.dao.entities.Audio
import models.dao.repositories.AudioRepository
import models.dto.AudioDTO
import scala.util.{Failure, Success, Try}

trait AudioService {
  /**
   * @param audio AudioDTO
   * @return Try[Audio]
   */
  def addAudio(audio: AudioDTO): Try[Audio]

  /**
   * @param audio AudioDTO
   * @return Try[String]
   */
  def updateAudio(audio: AudioDTO): Try[String]

  /**
   * @param id Int
   * @return Unit
   */
  def setReady(id: Int): Unit

  /**
   * @param id Int
   * @return Try[Audio]
   */
  def getAudioById(id: Int): Try[Audio]

  /**
   * @param userId Int
   * @return List[Video]
   */
  def getUserAudios(userId: Int): List[Audio]

  /**
   * @return List[Audio]
   */
  def getLastAudios: List[Audio]

  /**
   * @param id Int
   * @return Try[String]
   */
  def deleteAudio(id: Int): Try[String]
}

class AudioServiceImpl @Inject()(
  val audioRepository: AudioRepository,
  val logService: LogService
) extends AudioService {
  /**
   * @param audio AudioDTO
   * @return Try[Audio]
   */
  override def addAudio(audio: AudioDTO): Try[Audio] = try {
    Success(audioRepository.insert(audio.toModel))
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }

  /**
   * @param audio AudioDTO
   * @return Try[String]
   */
  def updateAudio(audio: AudioDTO): Try[String] = try {
    audioRepository.upgrade(audio.toModel)
    Success("Audio updated")
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }

  /**
   * @param id Int
   * @return Unit
   */
  override def setReady(id: Int): Unit = audioRepository.ready(id)

  /**
   * @param id Int
   * @return Try[Audio]
   */
  override def getAudioById(id: Int): Try[Audio] = {
    audioRepository.get(id) match {
      case None => Failure(new Throwable("Audio is not found"))
      case Some(audio) => Success(audio)
    }
  }

  /**
   * @param userId Int
   * @return List[Audio]
   */
  override def getUserAudios(userId: Int): List[Audio] = audioRepository.getByUserId(userId)

  /**
   * @return List[Video]
   */
  override def getLastAudios: List[Audio] = audioRepository.getAFew(10)

  /**
   * @param id Int
   * @return Try[String]
   */
  override def deleteAudio(id: Int): Try[String] = try {
    audioRepository.delete(id)
    Success("Audio deleted")
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }
}
