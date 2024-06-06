import models.RequestContext
import play.api.mvc.Request

package object controllers {
  implicit def rcToRequest[A](implicit rc: RequestContext[A]): Request[A] = rc.request
}
