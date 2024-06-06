package models

import play.api.mvc.Request

case class RequestContext[C](request: Request[C], userId: Option[String])
