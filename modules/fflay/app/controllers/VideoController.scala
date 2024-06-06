package controllers

import com.google.inject.Inject
import models.dto.{FileDTO, VideoDTO}
import models.services.{FFmpegService, UserService, VideoService}
import play.api.data.Forms.{boolean, nonEmptyText, number, text}
import play.api.data.{Form, Forms, Mapping}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class VideoController @Inject()(
  val userService: UserService,
  val ffmpegService: FFmpegService,
  val videoService: VideoService
) extends Authorization {
  val mapping: Mapping[VideoDTO] = Forms.mapping(
    "name" -> nonEmptyText(3),
    "link" -> text,
    "userId" -> number(1),
    "file" -> text,
    "id" -> number,
    "ready" -> boolean
  )(VideoDTO.apply)(VideoDTO.unapply)

  val form: Form[VideoDTO] = Form(mapping)

  /** Video adding page controller
   * @return Action[AnyContent]
   */
  def addVideo(): Action[AnyContent] = authorize { request =>
    userService.getUserById(request.userId.head.toInt) match {
      case Success(user) => Ok(views.html.addVideo(form, user))
      case Failure(_) => Redirect(routes.VideoController.userVideos())
    }
  }

  /** Video adding form validator
   * @return Action[AnyContent]
   */
  def addVideoSubmit() = authorize (parse.multipartFormData) { implicit request =>
    val user = userService.getUserById(request.userId.head.toInt).toOption.head

    form.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.addVideo(formWithErrors, user)),
      videoDTO => {
        val data = request.request.body.file("file").head
        val file: FileDTO = FileDTO(data.filename, data.contentType.get)

        ffmpegService.addToQueue(videoDTO, file)
        Redirect(routes.VideoController.userVideos())
      }
    )
  }

  /** User videos page
   * @return Action[AnyContent]
   */
  def userVideos(): Action[AnyContent] = authorize {request =>
    userService.getUserById(request.userId.head.toInt) match {
      case Success(user) => Ok(views.html.videos(videoService.getUserVideos(user.id), user))
      case Failure(_) => Redirect(routes.IndexController.index())
    }
  }
}

