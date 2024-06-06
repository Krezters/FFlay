package models.services

import com.google.inject.Inject
import models.dao.entities.Video
import models.dao.repositories.VideoRepository
import models.dto.VideoDTO
import scala.util.{Failure, Success, Try}

trait VideoService {
  /**
   * @param video VideoDTO
   * @return Try[Video]
   */
  def addVideo(video: VideoDTO): Try[Video]

  /**
   * @param video VideoDTO
   * @return Try[String]
   */
  def updateVideo(video: VideoDTO): Try[String]

  /**
   * @param id Int
   * @return Unit
   */
  def setReady(id: Int): Unit

  /**
   * @param id Int
   * @return Try[Video]
   */
  def getVideoById(id: Int): Try[Video]

  /**
   * @param userId Int
   * @return List[Video]
   */
  def getUserVideos(userId: Int): List[Video]

  /**
   * @return List[Video]
   */
  def getLastVideos: List[Video]

  /**
   * @param id Int
   * @return Try[String]
   */
  def deleteVideo(id: Int): Try[String]
}

class VideoServiceImpl @Inject()(
  val videoRepository: VideoRepository,
  val logService: LogService
) extends VideoService {
  /**
   * @param video VideoDTO
   * @return Try[Video]
   */
  override def addVideo(video: VideoDTO): Try[Video] = try {
    Success(videoRepository.insert(video.toModel))
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }

  /**
   * @param video VideoDTO
   * @return Try[String]
   */
  def updateVideo(video: VideoDTO): Try[String] = try {
    videoRepository.upgrade(video.toModel)
    Success("Video updated")
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }

  /**
   * @param id Int
   * @return Unit
   */
  override def setReady(id: Int): Unit = videoRepository.ready(id)

  /**
   * @param id Int
   * @return Try[Video]
   */
  override def getVideoById(id: Int): Try[Video] = {
    videoRepository.get(id) match {
      case None => Failure(new Throwable("Video is not found"))
      case Some(video) => Success(video)
    }
  }

  /**
   * @param userId Int
   * @return List[Video]
   */
  override def getUserVideos(userId: Int): List[Video] = videoRepository.getByUserId(userId)

  /**
   * @return List[Video]
   */
  override def getLastVideos: List[Video] = videoRepository.getAFew(10)

  /**
   * @param id Int
   * @return Try[String]
   */
  override def deleteVideo(id: Int): Try[String] = try {
    videoRepository.delete(id)
    Success("Video deleted")
  } catch {
    case e: Throwable => logService.log(e.getMessage)
      Failure(new Throwable("An error occurred, please try later"))
  }
}
