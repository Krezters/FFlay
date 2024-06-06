package di

import com.google.inject.Scopes
import net.codingwell.scalaguice.ScalaModule

abstract class AppModule extends ScalaModule{
  protected def bindSingleton[T, C <: T]
  (implicit t: Manifest[T], c: Manifest[C]): Unit = {
    bind(t.runtimeClass.asInstanceOf[Class[T]])
      .to(c.runtimeClass.asInstanceOf[Class[C]]).in(Scopes.SINGLETON)
  }
}
