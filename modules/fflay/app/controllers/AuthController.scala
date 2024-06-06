package controllers

import com.google.inject.Inject
import models.dto.UserDTO
import models.services.UserService
import play.api.data.{Form, Forms, Mapping}
import play.api.data.Forms.nonEmptyText
import play.api.mvc.{Action, AnyContent}
import scala.util.{Failure, Success}

class AuthController @Inject()(val userService: UserService) extends Authorization {
  val mapping: Mapping[UserDTO] = Forms.mapping(
    "name" -> nonEmptyText(3, 10),
    "password" -> nonEmptyText(5)
  )(UserDTO.apply)(UserDTO.unapply)

  val form: Form[UserDTO] = Form(mapping)

  /** Login page controller
   * @return Action[AnyContent]
   */
  def login: Action[AnyContent] = Action {
    Ok(views.html.login(form))
  }

  /** Login page from validator
   * @return Action[AnyContent]
   */
  def loginData(): Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      userDTO => userService.getUser(userDTO) match {
          case Failure(e) => BadRequest(views.html.login(form.withError("name", e.getMessage)))
          case Success(user) => Redirect(routes.IndexController.index()).withSession("userId" -> user.id.toString)
        }
    )
  }

  /** Registration page controller
   * @return Action[AnyContent]
   */
  def register: Action[AnyContent] = Action{
    Ok(views.html.register(form))
  }

  /** Registration page from validator
   * @return Action[AnyContent]
   */
  def registrationData(): Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.register(formWithErrors)),
      userDTO => {
        userService.addUser(userDTO) match {
          case Failure(e) => BadRequest(views.html.register(form.withError("name", e.getMessage)))
          case Success(user) => Redirect(routes.IndexController.index()).withSession("userId" -> user.id.toString)
        }
      }
    )
  }

  /** Logout controller
   * @return Action[AnyContent]
   */
  def logout: Action[AnyContent] = authorize {
    Redirect(routes.IndexController.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }
}

