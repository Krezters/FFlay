package models.services

import com.google.inject.Inject
import models.dao.entities.{Video, Audio}
import models.dto.{AudioDTO, FileDTO, VideoDTO}

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait FFmpegService {
  /**
   * Adding a file to the processing queue
   *
   * @param format VideoDTO or AudioDTO
   * @param fileDTO FileDTO
   * @param ex ExecutionContext
   * @tparam F VideoDTO or AudioDTO
   * @return Future[F]
   */
  def addToQueue[F](format: F, fileDTO: FileDTO)(implicit ex: ExecutionContext): Future[F]

  /**
   * Get the latest ready-made files from the database
   * @return
   */
  def getLastContent: (List[Video], List[Audio])
}

class FFmpegServiceImpl @Inject()(
  val videoService: VideoService,
  val audioService: AudioService,
  val fileService: FileService
) extends FFmpegService {
  /**
   * Adding a file to the processing queue
   *
   * @param format VideoDTO or AudioDTO
   * @param fileDTO FileDTO
   * @param ex ExecutionContext
   * @tparam F VideoDTO or AudioDTO
   * @return Future[F]
   */
  override def addToQueue[F](format: F, fileDTO: FileDTO)(implicit ex: ExecutionContext): Future[F] = {
    val promise: Promise[F] = Promise[F]
    saveDTO(format, fileDTO) match {
      case Success(s) => makeJob(promise, format, s)
      case _ => promise.failure(new Throwable("Internal error"))
    }

    promise.future
  }

  private def saveDTO[F](format: F, fileDTO: FileDTO): Try[String] = {
    val path = fileService.save(fileDTO)
    val linkToStorage = path match {
      case Success(s) => s
      case _ => ""
    }

    format match {
      case video: VideoDTO => videoService.addVideo(video.update(link = linkToStorage))
      case audio: AudioDTO => audioService.addAudio(audio.update(link = linkToStorage))
    }

    path
  }

  private def makeJob[F](promise: Promise[F], format: F, path: String)(implicit ex: ExecutionContext): Promise[F] = {
    val name = format match {
      case v: VideoDTO => v.name
      case v: AudioDTO => v.name
    }

    val future: Future[Try[F]] = Future {
      Seq("ffmpeg", "-i", path, "formated-" + name + ".mp4")
      Success(format)
    }

    future.onComplete {
      case Failure(exception) => promise.failure(exception)
      case Success(format) => format match {
        case video: VideoDTO => videoService.setReady(video.id)
        case audio: AudioDTO => audioService.setReady(audio.id)
      }
        promise.complete(format)
    }
    promise
  }

  /**
   * Get the latest ready-made files from the database
   * @return
   */
  def getLastContent: (List[Video], List[Audio]) = (videoService.getLastVideos, audioService.getLastAudios)
}
