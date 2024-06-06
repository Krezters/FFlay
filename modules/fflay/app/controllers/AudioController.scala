package controllers

import com.google.inject.Inject
import models.dto.{FileDTO, AudioDTO}
import models.services.{FFmpegService, UserService, AudioService}
import play.api.data.Forms.{boolean, nonEmptyText, number, text}
import play.api.data.{Form, Forms, Mapping}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class AudioController @Inject()(
  val userService: UserService,
  val ffmpegService: FFmpegService,
  val audioService: AudioService
) extends Authorization {
  val mapping: Mapping[AudioDTO] = Forms.mapping(
    "name" -> nonEmptyText(3),
    "link" -> text,
    "userId" -> number(1),
    "file" -> nonEmptyText,
    "id" -> number,
    "ready" -> boolean
  )(AudioDTO.apply)(AudioDTO.unapply)

  val form: Form[AudioDTO] = Form(mapping)

  /** Audio adding page controller
   * @return Action[AnyContent]
   */
  def addAudio(): Action[AnyContent] = authorize { request =>
    userService.getUserById(request.userId.head.toInt) match {
      case Success(user) => Ok(views.html.addAudio(form, user))
      case Failure(_) => Redirect(routes.AudioController.userAudios())
    }
  }

  /** Audio adding form validator
   * @return Action[AnyContent]
   */
  def addAudioSubmit(): Action[AnyContent] = authorize { implicit request =>
    val user = userService.getUserById(request.userId.head.toInt).toOption.head

    form.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.addAudio(formWithErrors, user)),
      audioDTO => {
        val file: FileDTO = FileDTO(audioDTO.name, audioDTO.data)
        ffmpegService.addToQueue(audioDTO, file)
        Redirect(routes.AudioController.userAudios())
      }
    )
  }

  /** User audios page
   * @return Action[AnyContent]
   */
  def userAudios(): Action[AnyContent] = authorize {request =>
    userService.getUserById(request.userId.head.toInt) match {
      case Success(user) => Ok(views.html.audios(audioService.getUserAudios(user.id), user))
      case Failure(_) => Redirect(routes.IndexController.index())
    }
  }
}

