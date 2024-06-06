package controllers

import com.google.inject.Inject
import models.services.{FFmpegService, UserService}
import play.api.mvc.{Action, AnyContent}

import scala.util.{Failure, Success}

class IndexController @Inject()(
 val userService: UserService,
 val ffmpegService: FFmpegService
) extends Authorization {
  /** Home page
   * @return Action[AnyContent]
   */
  def index: Action[AnyContent] = Action { request =>
    val content = ffmpegService.getLastContent
    val unauthPage = Ok(views.html.index(content._1, content._2))

    request.session.get("userId") match {
      case None => unauthPage
      case Some(id) => userService.getUserById(id.toInt) match {
          case Success(user) => Ok(views.html.index(content._1, content._2, user))
          case Failure(_) => unauthPage
        }
    }
  }
}
