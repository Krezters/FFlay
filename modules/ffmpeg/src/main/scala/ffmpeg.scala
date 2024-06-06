import com.typesafe.config.ConfigFactory
import sys.process._

package object ffmpeg {
  private val configuration = ConfigFactory.load()

  def exec(e: Seq[String]) = e.!
//  private val url: String = configuration.getString("db.default.url")
//  private val user: String = configuration.getString("db.default.user")
//  private val password: String = configuration.getString("db.default.password")
}
