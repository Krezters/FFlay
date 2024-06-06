package controllers

import controllers.Assets.Forbidden
import models.RequestContext
import models.services.{AuthorizationService, AuthorizationServiceImpl}
import play.api.mvc.{ActionBuilder, ActionFilter, ActionTransformer, Controller, Request}

import scala.concurrent.Future

object RCAction extends ActionBuilder[RequestContext] with ActionTransformer[Request, RequestContext] {
  /**
   * @param request Request[A]
   * @tparam A Any
   * @return
   */
  override protected def transform[A](request: Request[A]): Future[RequestContext[A]] =
    Future.successful(RequestContext[A](request, request.session.get("userId")))
}

object PermissionCheckAction extends ActionFilter[RequestContext] {
  /**
   * @return AuthorizationService
   */
  private def authorizationService: AuthorizationService = new AuthorizationServiceImpl

  /**
   * @param in RequestContext[A]
   * @tparam A Any
   * @return
   */
  def filter[A](in: RequestContext[A]): Future[Option[Assets.Status]] = Future.successful{
    if(!authorizationService.check(in.userId)) Some(Forbidden)
    else None
  }
}

trait Authorization extends Controller {
  /**
   * @return ActionBuilder[RequestContext]
   */
  def authorize: ActionBuilder[RequestContext] = RCAction andThen PermissionCheckAction
}
